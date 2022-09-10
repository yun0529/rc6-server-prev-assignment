package com.example.demo.src.house;


import com.example.demo.config.BaseException;
import com.example.demo.src.Video.model.GetVideoRes;
import com.example.demo.src.Video.model.GetVideoWatchRes;
import com.example.demo.src.house.model.GetDetailContent;
import com.example.demo.src.house.model.PostRoomContentReq;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class HouseProvider {

    private final HouseDao houseDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HouseProvider(HouseDao houseDao, JwtService jwtService) {
        this.houseDao = houseDao;
        this.jwtService = jwtService;
    }

    public List<PostRoomContentReq> getContent(int houseIdx, int roomIdx) throws BaseException{
        try{
            List<PostRoomContentReq> getContentLists = houseDao.getContent(houseIdx, roomIdx);
            return getContentLists;
        }
        catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetDetailContent> getDetailContent(int houseIdx, int roomContentIdx) throws BaseException {
        try {
            List<GetDetailContent> getDetailContentRes = houseDao.getDetailContent(houseIdx, roomContentIdx);
            return getDetailContentRes;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
