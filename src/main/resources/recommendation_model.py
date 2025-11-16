# -*- coding: utf-8 -*-
# 简单的电影推荐模型示例
# 这里使用基于用户的协同过滤算法

# 模拟用户评分数据
user_ratings = {
    1: {1: 5, 2: 4, 3: 3},  # 用户1对电影1评5分，电影2评4分，电影3评3分
    2: {1: 4, 3: 5, 4: 2},  # 用户2对电影1评4分，电影3评5分，电影4评2分
    3: {2: 5, 3: 4, 5: 3},  # 用户3对电影2评5分，电影3评4分，电影5评3分
    4: {1: 3, 4: 5, 5: 4}   # 用户4对电影1评3分，电影4评5分，电影5评4分
}

def get_recommendations(user_id):
    """
    获取用户的电影推荐
    :param user_id: 用户ID
    :return: 推荐的电影列表
    """
    print("DEBUG: Input user_id: %s, type: %s" % (user_id, type(user_id)))
    try:
        user_id = int(user_id)
        print("DEBUG: Converted user_id: %s, type: %s" % (user_id, type(user_id)))
    except ValueError as e:
        print("DEBUG: Failed to convert user_id: %s" % e)
        return []
    
    if user_id not in user_ratings:
        print("DEBUG: User %s not in user_ratings" % user_id)
        return []
    
    print("DEBUG: User %s rated movies: %s" % (user_id, user_ratings[user_id]))
    user_rated_movies = set(user_ratings[user_id].keys())
    print("DEBUG: User rated movies set: %s" % user_rated_movies)
    
    all_movies = set()
    for ratings in user_ratings.values():
        all_movies.update(ratings.keys())
    print("DEBUG: All movies: %s" % all_movies)
    
    unrated_movies = all_movies - user_rated_movies
    print("DEBUG: Unrated movies for user %s: %s" % (user_id, unrated_movies))
    
    movie_ratings = {}
    for movie in unrated_movies:
        total = 0
        count = 0
        for ratings in user_ratings.values():
            if movie in ratings:
                total += ratings[movie]
                count += 1
        if count > 0:
            movie_ratings[movie] = total / count
    print("DEBUG: Movie ratings for unrated movies: %s" % movie_ratings)
    
    sorted_movies = sorted(movie_ratings.items(), key=lambda x: x[1], reverse=True)
    print("DEBUG: Sorted movies: %s" % sorted_movies)
    
    result = [movie for movie, rating in sorted_movies[:3]]
    print("DEBUG: Final recommendation result: %s" % result)
    return result

# 测试
if __name__ == "__main__":
    user_id = 1
    recommendations = get_recommendations(user_id)
    print("用户%s的推荐电影: %s" % (user_id, recommendations))
