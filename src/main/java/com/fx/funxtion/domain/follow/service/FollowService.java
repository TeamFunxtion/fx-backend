package com.fx.funxtion.domain.follow.service;

import com.fx.funxtion.domain.chat.dto.ChatRoomCreateRequest;
import com.fx.funxtion.domain.chat.entity.ChatRoom;
import com.fx.funxtion.domain.follow.dto.FollowUpdateRequest;
import com.fx.funxtion.domain.follow.dto.FollowerListResponse;
import com.fx.funxtion.domain.follow.dto.FollowingListResponse;
import com.fx.funxtion.domain.follow.entity.UserFollows;
import com.fx.funxtion.domain.follow.repository.FollowRepository;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.entity.Product;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {


    private final FollowRepository followRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public List<FollowerListResponse> getFollowerList(Long toMemberId) {
        List<UserFollows> list = followRepository.findAllByToMemberId(toMemberId);
        List<FollowerListResponse> followerListResponses = new ArrayList<>();

        for(UserFollows userFollows : list) {
            // 해당 유저(나를 팔로우 중인 유저)의 총 상품 수
            Long prCnt =  productRepository.countByMemberId(userFollows.getFromMember().getId());
            // 해당 유저(나를 팔로우 중인 유저)의 팔로워 수
            Long followerCnt = followRepository.countByToMemberId(userFollows.getFromMember().getId());
            // 로그인한 유저(나)가 해당 유저(나를 팔로우 중인 유저)를 팔로우 중인지 여부 체크
            boolean isFollowing = followRepository.existsByFromMemberIdAndToMemberId(toMemberId, userFollows.getFromMember().getId());
            followerListResponses.add(new FollowerListResponse(userFollows,prCnt, followerCnt, isFollowing));
        }

        return followerListResponses;
    }

    public List<FollowingListResponse> getFollowingList(Long fromId) {
        List<UserFollows> list = followRepository.findAllByFromMemberId(fromId);
        List<FollowingListResponse> followingListResponses = new ArrayList<>();

        for(UserFollows userFollows : list) {
            // 해당 유저(내가 팔로우하는 유저)의 총 상품 수
            Long prCnt =  productRepository.countByMemberId(userFollows.getToMember().getId());
            // 해당 유저(내가 팔로우하는 유저)의 팔로워 수
            Long followerCnt = followRepository.countByToMemberId(userFollows.getToMember().getId());
            // 로그인한 유저(나)가 해당 유저를 팔로우 중인지 여부 체크
            boolean isFollowing = followRepository.existsByFromMemberIdAndToMemberId(fromId, userFollows.getToMember().getId());
            followingListResponses.add(new FollowingListResponse(userFollows, prCnt, followerCnt, isFollowing));
        }
        return followingListResponses;
    }

    public Long updateFollow(FollowUpdateRequest followUpdateRequest) {
        Member toMember = memberRepository.findById(followUpdateRequest.getToId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        Member fromMember = memberRepository.findById(followUpdateRequest.getFromId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        UserFollows userFollowsEx = followRepository.findByFromMemberIdAndToMemberId(followUpdateRequest.getFromId(), followUpdateRequest.getToId());
        UserFollows uf;

        if(userFollowsEx == null) {
            UserFollows userFollows = UserFollows.builder()
                    .toMember(toMember)
                    .fromMember(fromMember)
                    .build();
            uf = followRepository.save(userFollows);
            return uf.getId();
        } else {
                followRepository.delete(userFollowsEx);
                return null;
        }
    }
}
