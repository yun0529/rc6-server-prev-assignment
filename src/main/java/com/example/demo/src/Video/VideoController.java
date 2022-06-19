package com.example.demo.src.Video;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Video.model.*;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/videos")
public class VideoController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VideoProvider videoProvider;
    @Autowired
    private final VideoService videoService;
    @Autowired
    private final JwtService jwtService;




    public VideoController(VideoProvider videoProvider, VideoService videoService, JwtService jwtService){
        this.videoProvider = videoProvider;
        this.videoService = videoService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 동영상 조회 API
     * [GET] /videos
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/videos
    public BaseResponse<List<GetVideoRes>> getUsers() {
        try{
            List<GetVideoRes> getVideoRes = videoProvider.getVideos();
            return new BaseResponse<>(getVideoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 영상 재생 화면 API
     * [GET] /videos/watch/:userIdx/:videoIdx
     * @return BaseResponse<GetVideoWatchRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/watch/{userIdx}/{videoIdx}") // (GET) 127.0.0.1:9000/videos/watch/:userIdx/:videoIdx
    public BaseResponse<GetVideoWatchRes> getVideoWatch(@PathVariable("userIdx") int userIdx, @PathVariable("videoIdx") int videoIdx) {
        // Get Users
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetVideoWatchRes getVideoWatchRes = videoProvider.getVideoWatch(videoIdx);
            return new BaseResponse<>(getVideoWatchRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 동영상 업로드 API
     * [POST] /videos
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> uploadVideo(@RequestBody PostVideoReq postVideoReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postVideoReq.getVideoTitle() == null){
            return new BaseResponse<>(POST_VIDEOS_EMPTY_TITLE);
        }
        String result = "";
        try{
            videoService.uploadUser(postVideoReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동영상 수정 API
     * [PATCH] /videos/modify
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/modify") // (PATCH) 127.0.0.1:9000/videos/modify
    public BaseResponse<String> modifyVideo(@RequestBody ModifyVideos modifyVideos){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyVideos.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            videoService.modifyVideo(modifyVideos);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동영상 좋아요 API
     * [POST] /videos/like-set
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/like-set")
    public BaseResponse<String> videoLikeSet(@RequestBody PostVideoLikeSetReq postVideoLikeSetReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postVideoLikeSetReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            videoService.videoLikeSet(postVideoLikeSetReq);
            return new BaseResponse<>("");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동영상 좋아요 해제 API
     * [PATCH] /videos/like-cancel
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/like-cancel")
    public BaseResponse<String> modifyAlarmSet(@RequestBody PostVideoLikeSetReq postVideoLikeSetReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postVideoLikeSetReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            videoService.videoLikeCancel(postVideoLikeSetReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
