package com.example.demo.src.Video;


import com.example.demo.config.BaseException;
import com.example.demo.src.Video.model.ModifyVideos;
import com.example.demo.src.Video.model.PostVideoLikeSetReq;
import com.example.demo.src.Video.model.PostVideoReq;
import com.example.demo.src.user.model.PostAlarmSetReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class VideoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VideoDao videoDao;
    private final VideoProvider videoProvider;
    private final JwtService jwtService;


    @Autowired
    public VideoService(VideoDao videoDao, VideoProvider videoProvider, JwtService jwtService) {
        this.videoDao = videoDao;
        this.videoProvider = videoProvider;
        this.jwtService = jwtService;

    }

    //POST
    public void uploadUser(PostVideoReq postVideoReq) throws BaseException {
        //중복
//        if(videoProvider.checkEmail(postUserReq.getUserAccount()) ==1){
//            throw new BaseException(POST_USERS_EXISTS_EMAIL);
//        }
        try{
            videoDao.uploadVideo(postVideoReq);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyVideo(ModifyVideos modifyVideos) throws BaseException {
        User user = videoDao.getUserIdx(modifyVideos.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = videoDao.modifyVideo(modifyVideos);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_VIDEO);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void videoLikeSet(PostVideoLikeSetReq postVideoLikeSetReq) throws BaseException {
        User user = videoDao.getUserIdx(postVideoLikeSetReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            if(videoDao.checkVideoLikeSetLog(postVideoLikeSetReq) == 1){
                videoDao.videoLikeSetExist(postVideoLikeSetReq);
            }else{
                videoDao.videoLikeSet(postVideoLikeSetReq);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void videoLikeCancel(PostVideoLikeSetReq postVideoLikeSetReq) throws BaseException {
        User user = videoDao.getUserIdx(postVideoLikeSetReq.getUserIdx());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(ACCESS_TO_LOGIN);
        }
        try{
            int result = videoDao.modifyVideoLikeSet(postVideoLikeSetReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_VIDEO_LIKE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
