package com.example.demo.src.Video;


import com.example.demo.src.Video.model.*;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class VideoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetVideoRes> getVideos(){
        String getUsersQuery = "select Video.videoIdx, User.userIdx, videoThumbnailImageUrl, videoUrl, userImageUrl, videoTitle, userNickName, " +
                "viewCount, case " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) < 1 and " +
                "minute(now()) - minute(Video.createdAt) < 1) then concat(second(now()) - second(Video.createdAt), '초 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) < 1 and " +
                "minute(now()) - minute(Video.createdAt) >= 1) then concat(minute(now()) - minute(Video.createdAt), '분 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Video.createdAt), '시간 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) >= 1 and dayofyear(now()) - dayofyear(Video.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Video.createdAt), '일 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Video.createdAt), '개월 전') end as createdAt " +
                "from Video " +
                "join YouTubeDB.User on Video.userIdx = User.userIdx " +
                "join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "order by createdAt asc";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetVideoRes(
                        rs.getInt("videoIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("videoThumbnailImageUrl"),
                        rs.getString("videoUrl"),
                        rs.getString("videoTitle"),
                        rs.getString("userNickname"),
                        rs.getInt("viewCount"),
                        rs.getString("createdAt"))
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

    public GetVideoWatchRes getVideoWatch(int userIdx){
        String getUserQuery = "select Video.videoIdx, User.userIdx, videoThumbnailImageUrl, videoUrl, userImageUrl, videoTitle, userNickName, " +
                "viewCount, case " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) < 1 and " +
                "minute(now()) - minute(Video.createdAt) < 1) then concat(second(now()) - second(Video.createdAt), '초 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) < 1 and " +
                "minute(now()) - minute(Video.createdAt) >= 1) then concat(minute(now()) - minute(Video.createdAt), '분 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) < 1 and hour(now()) - hour(Video.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Video.createdAt), '시간 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) >= 1 and dayofyear(now()) - dayofyear(Video.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Video.createdAt), '일 전') " +
                "when (year(now()) = year(Video.createdAt) and dayofyear(now()) - dayofyear(Video.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Video.createdAt), '개월 전') end as createdAt, " +
                "count(case when VideoLike.videoIdx = Video.videoIdx then 1 end ) as LikeCount, 'N' as unLike " +
                "from Video " +
                "join YouTubeDB.User on Video.userIdx = User.userIdx " +
                "join VideoViewCount on Video.videoIdx = VideoViewCount.videoIdx " +
                "join VideoLike " +
                "group by Video.videoIdx " +
                "having Video.videoIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetVideoWatchRes(
                        rs.getInt("videoIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("videoThumbnailImageUrl"),
                        rs.getString("videoUrl"),
                        rs.getString("videoTitle"),
                        rs.getString("userNickname"),
                        rs.getInt("viewCount"),
                        rs.getString("createdAt"),
                        rs.getInt("likeCount"),
                        rs.getString("unLike"),
                        getVideos()),
                getUserParams);
    }
    

    public int uploadVideo(PostVideoReq postVideoReq){
        String createUserQuery = "insert into Video (userIdx, videoThumbnailImageUrl, videoUrl, videoTitle," +
                " videoTime, videoComment, videoLocation, videoListIdx, videoChild, videoAge, videoTag, videoChatAllow, videoCategory) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postVideoReq.getUserIdx(), postVideoReq.getVideoThumbnailImageUrl(), postVideoReq.getVideoUrl(), postVideoReq.getVideoTitle(),
                postVideoReq.getVideoTime(), postVideoReq.getVideoComment(), postVideoReq.getVideoLocation(), postVideoReq.getVideoListIdx(), postVideoReq.getVideoChild(),
                postVideoReq.getVideoAge(), postVideoReq.getVideoTag(), postVideoReq.getVideoChatAllow(), postVideoReq.getVideoCategory()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int modifyUserImageUrl(int userIdx,String userImageUrl){
        String modifyUserNameQuery = "update User set userImageUrl = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{userImageUrl, userIdx};

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

    public int modifyVideo(ModifyVideos modifyVideos){
        String modifyUserNameQuery = "update Video set videoTitle = ?,videoComment = ?, status = ?, videoChild = ?, videoAge = ?, " +
                "videoLocation = ?, videoListIdx = ?,videoTag = ? where videoIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{modifyVideos.getVideoTitle(), modifyVideos.getVideoComment(), modifyVideos.getStatus(),
                modifyVideos.getVideoChild(),modifyVideos.getVideoAge(),modifyVideos.getVideoLocation(),
                modifyVideos.getVideoListIdx(),modifyVideos.getVideoTag(), modifyVideos.getVideoIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int checkVideoLikeSetLog(PostVideoLikeSetReq postVideoLikeSetReq){
        String checkEmailQuery = "select exists(select status from VideoLike where userIdx = ? and videoIdx = ?)";
        //String checkEmailParams = userAccount;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                postVideoLikeSetReq.getUserIdx(),postVideoLikeSetReq.getVideoIdx());

    }
    public int videoLikeSet(PostVideoLikeSetReq postVideoLikeSetReq){
        String createUserQuery = "insert into VideoLike (userIdx, videoIdx) VALUES (?,?)";
        Object[] createUserParams = new Object[]{postVideoLikeSetReq.getUserIdx(), postVideoLikeSetReq.getVideoIdx()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
    public int videoLikeSetExist(PostVideoLikeSetReq postVideoLikeSetReq){
        String modifyUserNameQuery = "update VideoLike set status = ? where userIdx = ? and videoIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Active", postVideoLikeSetReq.getUserIdx(), postVideoLikeSetReq.getVideoIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyVideoLikeSet(PostVideoLikeSetReq postVideoLikeSetReq){
        String modifyUserNameQuery = "update VideoLike set status = ? where userIdx = ? and videoIdx = ?";
        Object[] modifyUserNameParams = new Object[]{"Inactive", postVideoLikeSetReq.getUserIdx(), postVideoLikeSetReq.getVideoIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

}
