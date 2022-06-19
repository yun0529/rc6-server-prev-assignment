package com.example.demo.src.Video;


import com.example.demo.config.BaseException;
import com.example.demo.src.Video.model.GetVideoRes;
import com.example.demo.src.Video.model.GetVideoWatchRes;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PostLoginReq;
import com.example.demo.src.user.model.PostLoginRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class VideoProvider {

    private final VideoDao videoDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public VideoProvider(VideoDao videoDao, JwtService jwtService) {
        this.videoDao = videoDao;
        this.jwtService = jwtService;
    }

    public List<GetVideoRes> getVideos() throws BaseException{
        try{
            List<GetVideoRes> getUserRes = videoDao.getVideos();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(int userIdx) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = videoDao.getUsersByEmail(userIdx);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetVideoWatchRes getVideoWatch(int videoIdx) throws BaseException {
        try {
            GetVideoWatchRes getVideoWatchRes = videoDao.getVideoWatch(videoIdx);
            return getVideoWatchRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
