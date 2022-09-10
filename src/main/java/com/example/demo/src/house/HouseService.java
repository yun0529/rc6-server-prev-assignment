package com.example.demo.src.house;


import com.example.demo.config.BaseException;
import com.example.demo.src.Video.model.ModifyVideos;
import com.example.demo.src.Video.model.PostVideoLikeSetReq;
import com.example.demo.src.Video.model.PostVideoReq;
import com.example.demo.src.house.model.PatchContentCount;
import com.example.demo.src.house.model.PostRoomContentReq;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class HouseService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HouseDao houseDao;
    private final HouseProvider houseProvider;
    private final JwtService jwtService;


    @Autowired
    public HouseService(HouseDao houseDao, HouseProvider houseProvider, JwtService jwtService) {
        this.houseDao = houseDao;
        this.houseProvider = houseProvider;
        this.jwtService = jwtService;

    }

    //POST
    public void postContent(PostRoomContentReq postRoomContentReq) throws BaseException {

        try{
            houseDao.postContent(postRoomContentReq);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyContentCount(PatchContentCount patchContentCount) throws BaseException {
//        User user = houseDao.getUserIdx(modifyVideos.getUserIdx());
//        if(user.getStatus().equals("Inactive")){
//            throw new BaseException(ACCESS_TO_LOGIN);
//        }
        try{
            int result = houseDao.modifyContentCount(patchContentCount);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_VIDEO);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteContent(int roomIdx, String name) throws BaseException {
//        User user = houseDao.getUserIdx(postVideoLikeSetReq.getUserIdx());
//        if(user.getStatus().equals("Inactive")){
//            throw new BaseException(ACCESS_TO_LOGIN);
//        }
        try{
                houseDao.deleteContent(roomIdx, name);

        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
