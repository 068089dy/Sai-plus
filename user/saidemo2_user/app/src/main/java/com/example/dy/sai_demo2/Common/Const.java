package com.example.dy.sai_demo2.Common;

public interface Const {
    //用户信息
    String KEY_USERNAME = "username";
    String KEY_PASSWD = "password";
    String KEY_LOGIN_SERVER = "login_server";
    String KEY_EMAIL = "email";
    String KEY_USER_ID = "user_id";
    String KEY_SAVE_PASSWORD = "save_password";
    String KEY_AUTO_LOGIN = "auto_login";
    String KEY_SCHOOL = "school";
    String KEY_STUDENT_ID = "student_id";

    //接口信息
    String SERVER_IP = "123.206.69.183";
    String LINK_USER_LOGIN = "/user_login/";//用户登录
    String LINK_CIRCLE_LIST_LOAD = "/circle_list_load/";//圈子列表
    String LINK_ARTICLE_LIST_LOAD = "/article_list_load/";//技术交流列表
    String LINK_TEAM_LIST_LOAD = "/team_list_load/";//团队组建列表
    String LINK_SCHEDULE_USER_INSERT= "/Schedule_user_insert/";//添加日程
    String LINK_MY_CICLE_LIST = "/my_circle_list/";//我的圈子
    String LINK_game_list_load = "/game_list_load/";//比赛列表
    String LINK_game_load_user = "/game_load_user/";//比赛详细信息
    String LINK_join_game = "/join_game/";//加入比赛
    String LINK_my_event_list_user = "/my_event_list_user/";//当前用户参加的招聘 和比赛表
    String LINK_message_list_load = "/message_list_load/";//招聘信息表
    String LINK_message_load_user = "/message_load_user/";//招聘信息具体内容
    String LINK_article_load = "/article_load/";//技术贴具体信息
    String LINK_user_article_insert = "/user_article_insert/";//用户技术贴添加
    String LINK_Schedule_list_load_user = "/Schedule_list_load_user/";//用户日程表
    String LINK_join_message = "/join_message/";
}
