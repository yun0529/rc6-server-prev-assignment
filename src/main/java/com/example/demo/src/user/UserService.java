package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getUserAccount()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUserPw());
            postUserReq.setUserPw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(userIdx,jwt);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserStatus(int userIdx) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(FAILED_TO_LOGOUT_STATUS);
        }
        try{
            int result = userDao.modifyUserStatus(userIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_STATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserImageUrl(int userIdx, String userImageUrl) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserImageUrl(userIdx, userImageUrl);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_IMAGE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserBackgroundImageUrl(int userIdx, String userBackgroundImageUrl) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserBackgroundImageUrl(userIdx, userBackgroundImageUrl);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_BACKGROUND_IMAGE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserNickname(int userIdx, String userNickname) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserNickname(userIdx, userNickname);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_NICKNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserComment(int userIdx, String userComment) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserComment(userIdx, userComment);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_COMMENT);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserSubscribeAccess(int userIdx, String userSubscribeAccess) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserSubscribeAccess(userIdx, userSubscribeAccess);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_SUBSCRIBE_ACCESS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserSavePlayListAccess(int userIdx, String userSavePlayListAccess) throws BaseException {
        User user = userDao.getUserIdx(userIdx);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = userDao.modifyUserSavePlayListAccess(userIdx, userSavePlayListAccess);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USER_SAVE_PLAYLIST_ACCESS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
