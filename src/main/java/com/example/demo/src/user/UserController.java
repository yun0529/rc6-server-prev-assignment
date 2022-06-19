package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) int userIdx) {
        try{
            if(userIdx == 0){
                List<GetUserRes> getUsersRes = userProvider.getUsers();
                return new BaseResponse<>(getUsersRes);
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(userIdx);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 다른 유저 채널 조회 API
     * [GET] /users/other-chennel/:userIdx
     * @return BaseResponse<GetUserRes>
     * JWT 없어도 되는 API
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/other-chennel/{userIdx}/{otherUserIdx}") // (GET) 127.0.0.1:9000/users/other-chennel/:userIdx/:otherUserIdx
    public BaseResponse<GetOtherUserRes> getOtherUser(@PathVariable("userIdx") int userIdx, @PathVariable("otherUserIdx") int otherUserIdx) {
        // Get Users
        try{
            GetOtherUserRes getOtherUserRes = null;
            if(jwtService.getJwt().equals("")){
                //비회원용
                getOtherUserRes = userProvider.getOtherUser(otherUserIdx);
            }else{
                //회원용
                getOtherUserRes = userProvider.getOtherUser(userIdx,otherUserIdx);
                int userIdxByJwt = jwtService.getUserIdx();
                //userIdx와 접근한 유저가 같은지 확인
                if(userIdx != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            }

            return new BaseResponse<>(getOtherUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 구독한 채널 조회 API
     * [GET] /users/other-channel/:userIdx
     * @return BaseResponse<List<GetSubscribeUserRes>>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/subscribe-channel/{userIdx}") // (GET) 127.0.0.1:9000/users/other-channel/:userIdx/:otherUserIdx
    public BaseResponse<List<GetSubscribeUserRes>> getSubscribeUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            List<GetSubscribeUserRes> getOtherUserRes = userProvider.getSubscribeUser(userIdx);
                int userIdxByJwt = jwtService.getUserIdx();
                //userIdx와 접근한 유저가 같은지 확인
                if(userIdx != userIdxByJwt){
                    return new BaseResponse<>(INVALID_USER_JWT);
                }
            return new BaseResponse<>(getOtherUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getUserAccount() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getUserAccount())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그아웃 API
     * [PATCH] /users/logOut/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/logOut/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            //PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserNickname());
            userService.modifyUserStatus(userIdx);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 프로필 이미지 수정 API
     * [PATCH] /users/profileImage
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/profileImage")
    public BaseResponse<String> modifyUserImageUrl(@RequestBody ModifyUserImageUrl modifyUserImageUrl){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserImageUrl.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserImageUrl(modifyUserImageUrl.getUserIdx(), modifyUserImageUrl.getUserImageUrl());

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 배경 이미지 수정 API
     * [PATCH] /users/backgroundImage
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/backgroundImage")
    public BaseResponse<String> modifyUserBackgroundImageUrl(@RequestBody ModifyUserBackgroundImageUrl modifyUserBackgroundImageUrl){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserBackgroundImageUrl.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserBackgroundImageUrl(modifyUserBackgroundImageUrl.getUserIdx(), modifyUserBackgroundImageUrl.getUserBackgroundImageUrl());

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 이름 수정 API
     * [PATCH] /users/nickname
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/nickname")
    public BaseResponse<String> modifyUserNickname(@RequestBody ModifyUserNickname modifyUserNickname){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserNickname.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserNickname(modifyUserNickname.getUserIdx(), modifyUserNickname.getUserNickname());

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 설명 수정 API
     * [PATCH] /users/comment
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/comment")
    public BaseResponse<String> modifyUserComment(@RequestBody ModifyUserComment modifyUserComment){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserComment.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserComment(modifyUserComment.getUserIdx(), modifyUserComment.getUserComment());

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 구독정보 공개 여부 수정 API
     * [PATCH] /users/userSubscribeAccess
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("userSubscribeAccess")
    public BaseResponse<String> modifyUserSubscribeAccess(@RequestBody ModifyUserSubscribeAccess modifyUserSubscribeAccess){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserSubscribeAccess.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserSubscribeAccess(modifyUserSubscribeAccess.getUserIdx(), modifyUserSubscribeAccess.getUserSubscribeAccess());

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채널 재생목록 공개 여부 수정 API
     * [PATCH] /users/userSavePlayListAccess
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/userSavePlayListAccess")
    public BaseResponse<String> modifyUserSavePlayListAccess(@RequestBody ModifyUserSavePlayListAccess modifyUserSavePlayListAccess){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(modifyUserSavePlayListAccess.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            userService.modifyUserSavePlayListAccess(modifyUserSavePlayListAccess.getUserIdx(), modifyUserSavePlayListAccess.getUserSavePlayListAccess());

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구독하기 API
     * [POST] /users/subscribe
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/subscribe")
    public BaseResponse<String> subscribeUser(@RequestBody PostSubscribeUserReq postSubscribeUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postSubscribeUserReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.subscribeUser(postSubscribeUserReq);
            return new BaseResponse<>("");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구독 취소 API
     * [PATCH] /users/subscribe-cancel
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/subscribe-cancel")
    public BaseResponse<String> modifyUserSubscribe(@RequestBody PostSubscribeUserReq postSubscribeUserReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postSubscribeUserReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyUserSubscribe(postSubscribeUserReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 알림 설정 API
     * [POST] /users/alarm-set
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/alarm-set")
    public BaseResponse<String> alarmSet(@RequestBody PostAlarmSetReq postAlarmSetReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postAlarmSetReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.alarmSet(postAlarmSetReq);
            return new BaseResponse<>("");
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 알림설정 해제 API
     * [PATCH] /users/alarm-cancel
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/alarm-cancel")
    public BaseResponse<String> modifyAlarmSet(@RequestBody PostAlarmSetReq postAlarmSetReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(postAlarmSetReq.getUserIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyAlarmSet(postAlarmSetReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
