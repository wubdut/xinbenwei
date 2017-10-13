# 1. 配置文件位置
config/xbw.properties
# 2. 配置文件
    #redis地址
    redis_url=39.108.214.220
    #redis端口号
    redis_port=6379
    #redis链接超时时间
    redis_timeout=60000
    #redis密码
    redis_password=myRedis
    #是否进行模型更新 update_flag=true
    update_flag=false
    #是否进行联想计算 associate_flag=true
    associate_flag=false
    #是否进行推荐计算 recommend_flag=true
    recommend_flag=false
# 3. 依赖
    java 1.8
# 4. 启动方式
    java -jar XBW.jar
