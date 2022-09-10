package com.example.demo.src.house;


import com.example.demo.src.Video.model.*;
import com.example.demo.src.house.model.GetDetailContent;
import com.example.demo.src.house.model.PatchContentCount;
import com.example.demo.src.house.model.PostRoomContentReq;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HouseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<PostRoomContentReq> getContent(int houseIdx, int roomIdx){
        String getUsersQuery = "select RoomContent.roomIdx, contentName, contentType, contentCount, contentComment " +
                "from RoomContent " +
                "join Room on RoomContent.roomIdx = Room.roomIdx " +
                "where houseIdx = ? and Room.roomIdx = ? ";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new PostRoomContentReq(
                        rs.getInt("roomIdx"),
                        rs.getString("contentName"),
                        rs.getString("contentType"),
                        rs.getInt("contentCount"),
                        rs.getString("contentComment")), houseIdx, roomIdx
                );
    }

    public List<GetDetailContent> getDetailContent(int houseIdx, int roomContentIdx){
        String getUserQuery = "select RoomDetailContent.roomContentIdx, detailComment, detailType " +
                "from RoomDetailContent " +
                "left join RoomContent on RoomDetailContent.roomContentIdx = RoomContent.roomContentIdx " +
                "left join Room on RoomContent.roomIdx = Room.roomIdx " +
                "where houseIdx = ? and RoomDetailContent.roomContentIdx = ? ";
        int getUserParams = roomContentIdx;
        int getHouseParams = houseIdx;
        return this.jdbcTemplate.query(getUserQuery,
                (rs, rowNum) -> new GetDetailContent(
                        rs.getInt("roomContentIdx"),
                        rs.getString("detailComment"),
                        rs.getString("detailType")),
                getHouseParams, getUserParams);
    }
    

    public int postContent(PostRoomContentReq postRoomContentReq){
        String createUserQuery = "insert into RoomContent (roomIdx, contentName, contentType, contentCount, contentComment) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postRoomContentReq.getRoomIdx(), postRoomContentReq.getContentName(), postRoomContentReq.getContentType(),
                postRoomContentReq.getContentCount(), postRoomContentReq.getContentComment() };
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

    public int modifyContentCount(PatchContentCount patchContentCount){
        String modifyUserNameQuery = "update RoomContent set contentCount = ? where roomIdx = ? and contentName = ?";
        Object[] modifyUserNameParams = new Object[]{patchContentCount.getContentCount(), patchContentCount.getRoomIdx(), patchContentCount.getContentName()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
    public int deleteContent(int roomIdx, String name){
        String createUserQuery = "delete from RoomContent where roomIdx = ? and contentName = ? ";
        Object[] deleteContentParams = new Object[]{roomIdx, name};
        this.jdbcTemplate.update(createUserQuery, deleteContentParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }
}
