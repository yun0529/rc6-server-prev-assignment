package com.example.demo.src.house;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Video.model.*;
import com.example.demo.src.house.model.GetDetailContent;
import com.example.demo.src.house.model.PatchContentCount;
import com.example.demo.src.house.model.PostRoomContentReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.POST_VIDEOS_EMPTY_TITLE;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@RestController
@RequestMapping("/houses")
public class HouseController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HouseProvider houseProvider;
    @Autowired
    private final HouseService houseService;
    @Autowired
    private final JwtService jwtService;

    public HouseController(HouseProvider houseProvider, HouseService houseService, JwtService jwtService){
        this.houseProvider = houseProvider;
        this.houseService = houseService;
        this.jwtService = jwtService;
    }

    /**
     * 방 내용물 조회 API
     * [GET] /houses/{houseIdx}/{roomIdx}
     * @return BaseResponse<List<PostRoomContentReq>>
     */
    //Query String
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @GetMapping("{houseIdx}/{roomIdx}") // (GET) 127.0.0.1:9000/videos
    public BaseResponse<List<PostRoomContentReq>> getContent(@PathVariable("houseIdx") int houseIdx, @PathVariable("roomIdx") int roomIdx) {
        try{
            List<PostRoomContentReq> getContentLists = houseProvider.getContent(houseIdx, roomIdx);
            return new BaseResponse<>(getContentLists);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 세부 내용물 조회 API
     * [GET] /houses/{houseIdx}/detail/{roomContentIdx}
     * @return BaseResponse<List<GetDetailContent>>
     */
    // Path-variable
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @GetMapping("/{houseIdx}/detail/{roomContentIdx}") // (GET) 127.0.0.1:9000/videos/watch/:userIdx/:videoIdx
    public BaseResponse<List<GetDetailContent>> getDetailContent(@PathVariable("houseIdx") int houseIdx, @PathVariable("roomContentIdx") int roomContentIdx) {
        // Get Users
        try{
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            List<GetDetailContent> getDetailContentRes = houseProvider.getDetailContent(houseIdx, roomContentIdx);
            return new BaseResponse<>(getDetailContentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 내용물(가구) 등록 API
     * [POST] /houses/content
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("/content")
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    public BaseResponse<String> postContent(@RequestBody PostRoomContentReq postRoomContentReq) {
//        TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
//        if(postRoomContentReq.getVideoTitle() == null){
//            return new BaseResponse<>(POST_VIDEOS_EMPTY_TITLE);
//        }
        String result = "등록 완료";
        try{
            houseService.postContent(postRoomContentReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내용물 개수 수정 API
     * [PATCH] /house/content
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/content") // (PATCH) 127.0.0.1:9000/house/content/{roomIdx}/{contentName}
    public BaseResponse<String> modifyContentCount(@RequestBody PatchContentCount patchContentCount){
        try {
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(modifyVideos.getUserIdx() != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            //같다면 유저네임 변경
            houseService.modifyContentCount(patchContentCount);

            String result = "개수 수정 완료";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내용물 삭제 API
     * [Delete] /houses/content
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    @DeleteMapping("/content/{roomIdx}")
    public BaseResponse<String> deleteContent(@PathVariable("roomIdx") int roomIdx, @RequestParam(required = false) String name) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        try{
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(postVideoLikeSetReq.getUserIdx() != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            houseService.deleteContent(roomIdx, name);
            return new BaseResponse<>("삭제 완료");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
