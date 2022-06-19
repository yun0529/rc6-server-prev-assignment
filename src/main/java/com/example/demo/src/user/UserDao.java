package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "SELECT User.userIdx, userNickName, userImageUrl, userBackgroundImageUrl, " +
                "count(subscribeUserIdx) as subscribeCount, count(Video.userIdx) as videoCount, " +
                "userComment, userAccount,userSubscribeAccess,userSavePlayListAccess, User.createdAt, " +
                "sum(viewCount) as totalViews " +
                "from YouTubeDB.User " +
                "left join UserSubscribe on User.userIdx = UserSubscribe.subscribeUserIdx " +
                "left join Video on User.userIdx = Video.userIdx " +
                "left join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "group by User.userIdx ";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userImageUrl"),
                        rs.getString("userBackgroundImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("userComment"),
                        rs.getString("userAccount"),
                        rs.getString("userSubscribeAccess"),
                        rs.getString("userSavePlayListAccess"),
                        rs.getString("createdAt"),
                        rs.getString("totalViews"))
                );
    }

    public List<GetUserRes> getUsersByEmail(int userIdx){
        String getUsersByEmailQuery = "SELECT User.userIdx, userNickName, userImageUrl, userBackgroundImageUrl, " +
                "count(subscribeUserIdx) as subscribeCount, count(Video.userIdx) as videoCount, " +
                "userComment, userAccount,userSubscribeAccess,userSavePlayListAccess, User.createdAt, " +
                "sum(viewCount) as totalViews " +
                "from YouTubeDB.User " +
                "left join UserSubscribe on User.userIdx = UserSubscribe.subscribeUserIdx " +
                "left join Video on User.userIdx = Video.userIdx " +
                "left join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "group by User.userIdx " +
                "having User.userIdx = ?";
        int getUsersByEmailParams = userIdx;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userImageUrl"),
                        rs.getString("userBackgroundImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("userComment"),
                        rs.getString("userAccount"),
                        rs.getString("userSubscribeAccess"),
                        rs.getString("userSavePlayListAccess"),
                        rs.getString("createdAt"),
                        rs.getString("totalViews")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "SELECT User.userIdx, userNickName, userImageUrl, userBackgroundImageUrl, " +
                "count(subscribeUserIdx) as subscribeCount, count(Video.userIdx) as videoCount, " +
                "userComment, userAccount,userSubscribeAccess,userSavePlayListAccess, User.createdAt, " +
                "sum(viewCount) as totalViews " +
                "from YouTubeDB.User " +
                "left join UserSubscribe on User.userIdx = UserSubscribe.subscribeUserIdx " +
                "left join Video on User.userIdx = Video.userIdx " +
                "left join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "group by User.userIdx " +
                "having User.userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userImageUrl"),
                        rs.getString("userBackgroundImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("userComment"),
                        rs.getString("userAccount"),
                        rs.getString("userSubscribeAccess"),
                        rs.getString("userSavePlayListAccess"),
                        rs.getString("createdAt"),
                        rs.getString("totalViews")),
                getUserParams);
    }
    public GetOtherUserRes getOtherUser(int otherUserIdx){
        //비회원용
        String getUserQuery = "SELECT " +
                "User.userIdx, userNickName, userImageUrl, userBackgroundImageUrl, " +
                "count(subscribeUserIdx) as subscribeCount, count(Video.userIdx) as videoCount, " +
                "userComment, User.createdAt, " +
                "sum(viewCount) as totalViews " +
                "from YouTubeDB.User " +
                "left join UserSubscribe on User.userIdx = UserSubscribe.subscribeUserIdx " +
                "left join Video on User.userIdx = Video.userIdx " +
                "left join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "where User.userIdx = ?";
        int getOtherUserParams = otherUserIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetOtherUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userImageUrl"),
                        rs.getString("userBackgroundImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("userComment"),
                        rs.getString("createdAt"),
                        rs.getString("totalViews")),
                getOtherUserParams);
    }
    public GetOtherUserRes getOtherUser(int userIdx, int otherUserIdx){
        // 회원용
        String getUserQuery = "SELECT " +
                "User.userIdx, userNickName, userImageUrl, userBackgroundImageUrl, " +
                "count(subscribeUserIdx) as subscribeCount, count(Video.userIdx) as videoCount, " +
                "userComment, User.createdAt, " +
                "case " +
                "when (exists(select UserSubscribe.userIdx, subscribeUserIdx from UserSubscribe where UserSubscribe.userIdx = ? and UserSubscribe.subscribeUserIdx = ?)) then 'Y' " +
                "else 'N' end as subscribe, " +
                "case " +
                "when (exists(select AlarmSet.userIdx, alarmUserIdx from AlarmSet where AlarmSet.userIdx = ? and alarmUserIdx = ?)) then 'Y' " +
                "else 'N' end as alarmSet, " +
                "sum(viewCount) as totalViews " +
                "from YouTubeDB.User " +
                "left join UserSubscribe on User.userIdx = UserSubscribe.subscribeUserIdx " +
                "left join Video on User.userIdx = Video.userIdx " +
                "left join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "where User.userIdx = ?";
        int getUserParams = userIdx;
        int getOtherUserParams = otherUserIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetOtherUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userImageUrl"),
                        rs.getString("userBackgroundImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("userComment"),
                        rs.getString("createdAt"),
                        rs.getString("subscribe"),
                        rs.getString("alarmSet"),
                        rs.getString("totalViews")),
                getUserParams, getOtherUserParams, getUserParams, getOtherUserParams, getOtherUserParams);
    }

    public List<GetSubscribeUserRes> getSubscribeUser(int userIdx){
        //비회원용
        String getUserQuery = "select " +
                "UserSubscribe.subscribeUserIdx, (select userNickName from YouTubeDB.User where User.userIdx = UserSubscribe.subscribeUserIdx) as subscribeUserNickname, " +
                "(select userImageUrl from YouTubeDB.User where User.userIdx = UserSubscribe.subscribeUserIdx) as subscribeUserImageUrl, " +
                "(select count(subscribeUserIdx) from YouTubeDB.User where userIdx = UserSubscribe.subscribeUserIdx) as subscribeCount, " +
                "(select count(Video.userIdx) from YouTubeDB.User join Video on User.userIdx = Video.userIdx where User.userIdx = UserSubscribe.subscribeUserIdx) as videoCount, " +
                "(select " +
                "case " +
                "when (exists(select UserSubscribe.userIdx, subscribeUserIdx from UserSubscribe " +
                "where UserSubscribe.userIdx = ? and UserSubscribe.subscribeUserIdx = UserSubscribe.subscribeUserIdx)) then 'Y' " +
                "else 'N' end as subscribe " +
                "from YouTubeDB.User where User.userIdx = UserSubscribe.subscribeUserIdx) as subscribe, " +
                "(select " +
                "case " +
                "when (exists(select AlarmSet.userIdx, alarmUserIdx from AlarmSet where AlarmSet.userIdx = ? and alarmUserIdx = UserSubscribe.subscribeUserIdx)) then 'Y' " +
                "else 'N' end as alarmSet " +
                "from YouTubeDB.User where User.userIdx = UserSubscribe.subscribeUserIdx) as alarmSet " +
                "from UserSubscribe " +
                "left join YouTubeDB.User on UserSubscribe.userIdx = User.userIdx " +
                "where UserSubscribe.userIdx = ? " +
                "group by subscribeUserIdx";
        int getOtherUserParams = userIdx;
        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetSubscribeUserRes(
                        rs.getInt("subscribeUserIdx"),
                        rs.getString("subscribeUserNickname"),
                        rs.getString("subscribeUserImageUrl"),
                        rs.getInt("subscribeCount"),
                        rs.getInt("videoCount"),
                        rs.getString("subscribe"),
                        rs.getString("alarmSet")),
                getOtherUserParams,getOtherUserParams,getOtherUserParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userAccount, userPw, userImageUrl, userBackgroundImageUrl," +
                " userNickname, userName, userComment, nationalityIdx, userPhoneNumber, userBirth, userGender, userSubscribeAccess, userSavePlayListAccess) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserAccount(), postUserReq.getUserPw(), postUserReq.getUserImageUrl(),
                postUserReq.getUserBackgroundImageUrl(), postUserReq.getUserNickname(), postUserReq.getUserName(), postUserReq.getUserComment(),
                postUserReq.getNationalityIdx(), postUserReq.getUserPhoneNumber(), postUserReq.getUserBirth(), postUserReq.getUserGender(),
                postUserReq.getUserSubscribeAccess(), postUserReq.getUserSavePlayListAccess()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String userAccount){
        String checkEmailQuery = "select exists(select userAccount from User where userAccount = ?)";
        String checkEmailParams = userAccount;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserStatusLogIn(int userIdx){
        String modifyUserNameQuery = "update User set status = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{"Active", userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserImageUrl(int userIdx,String userImageUrl){
        String modifyUserNameQuery = "update User set userImageUrl = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userImageUrl, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserBackgroundImageUrl(int userIdx,String userBackgroundImageUrl){
        String modifyUserNameQuery = "update User set userBackgroundImageUrl = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userBackgroundImageUrl, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserNickname(int userIdx,String userNickname){
        String modifyUserNameQuery = "update User set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userNickname, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserComment(int userIdx,String userComment){
        String modifyUserNameQuery = "update User set userComment = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userComment, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserSubscribeAccess(int userIdx,String userSubscribeAccess){
        String modifyUserNameQuery = "update User set userSubscribeAccess = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userSubscribeAccess, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserSavePlayListAccess(int userIdx,String userSavePlayListAccess){
        String modifyUserNameQuery = "update User set userSavePlayListAccess = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userSavePlayListAccess, userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserStatus(int userIdx){
        String modifyUserNameQuery = "update User set status = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{"Inactive", userIdx};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, userNickname, userAccount,userPw,status from User where userAccount = ?";
        String getPwdParams = postLoginReq.getUserAccount();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userAccount"),
                        rs.getString("userPw"),
                        rs.getString("status")
                ), getPwdParams);

    }
    public User getUserIdx(int userIdx){
        String getPwdQuery = "select userIdx, userNickname, userAccount, userPw, status from User where userIdx = ?";
        int getPwdParams = userIdx;

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("userNickname"),
                        rs.getString("userAccount"),
                        rs.getString("userPw"),
                        rs.getString("status")
                ), getPwdParams);

    }
    public int checkSubscribeLog(PostSubscribeUserReq postSubscribeUserReq){
        String checkEmailQuery = "select exists(select status from UserSubscribe where userIdx = ? and subscribeUserIdx = ?)";
        //String checkEmailParams = userAccount;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                postSubscribeUserReq.getUserIdx(),postSubscribeUserReq.getSubscribeUserIdx());

    }
    public int subscribeUser(PostSubscribeUserReq postSubscribeUserReq){
        String createUserQuery = "insert into UserSubscribe (userIdx, subscribeUserIdx) VALUES (?,?)";
        Object[] createUserParams = new Object[]{postSubscribeUserReq.getUserIdx(), postSubscribeUserReq.getSubscribeUserIdx()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    public int subscribeUserExist(PostSubscribeUserReq postSubscribeUserReq){
        String modifyUserNameQuery = "update UserSubscribe set status = ? where userIdx = ? and subscribeUserIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Active", postSubscribeUserReq.getUserIdx(), postSubscribeUserReq.getSubscribeUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
    public int modifySubscribeUser(PostSubscribeUserReq postSubscribeUserReq){
        String modifyUserNameQuery = "update UserSubscribe set status = ? where userIdx = ? and subscribeUserIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Inactive", postSubscribeUserReq.getUserIdx(), postSubscribeUserReq.getSubscribeUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int checkAlarmSetLog(PostAlarmSetReq postAlarmSetReq){
        String checkEmailQuery = "select exists(select status from AlarmSet where userIdx = ? and alarmUserIdx = ?)";
        //String checkEmailParams = userAccount;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                postAlarmSetReq.getUserIdx(),postAlarmSetReq.getAlarmUserIdx());

    }
    public int alarmSet(PostAlarmSetReq postAlarmSetReq){
        String createUserQuery = "insert into AlarmSet (userIdx, alarmUserIdx) VALUES (?,?)";
        Object[] createUserParams = new Object[]{postAlarmSetReq.getUserIdx(), postAlarmSetReq.getAlarmUserIdx()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    public int alarmSetExist(PostAlarmSetReq postAlarmSetReq){
        String modifyUserNameQuery = "update AlarmSet set status = ? where userIdx = ? and alarmUserIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Active", postAlarmSetReq.getUserIdx(), postAlarmSetReq.getAlarmUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyAlarmSet(PostAlarmSetReq postAlarmSetReq){
        String modifyUserNameQuery = "update AlarmSet set status = ? where userIdx = ? and alarmUserIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Inactive", postAlarmSetReq.getUserIdx(), postAlarmSetReq.getAlarmUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }



}
