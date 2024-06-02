package com.fx.funxtion.domain.member.service;

import com.fx.funxtion.domain.member.dto.*;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.entity.UserFollows;
import com.fx.funxtion.domain.member.repository.FollowRepository;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.member.repository.ReviewRepository;
import com.fx.funxtion.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public Page<FollowerListResponse> getFollowerList(Long toMemberId, int page, int size) throws Exception {
        List<UserFollows> list = followRepository.findAllByToMemberId(toMemberId);
        Page<UserFollows> userFollowsPage = followRepository.findAllByToMemberId(toMemberId, PageRequest.of(page, size));

        return userFollowsPage.map(userFollows -> {
            Long productCnt = productRepository.countByMemberId(userFollows.getFromMember().getId());
            Long followerCnt = followRepository.countByToMemberId(userFollows.getFromMember().getId());
            boolean isFollowing = followRepository.existsByFromMemberIdAndToMemberId(toMemberId, userFollows.getFromMember().getId());
            int followCnt = list.size();
            // 현재 팔로워에 대한 평점
            Double avg = reviewRepository.findByAvgRatingBySellerId(userFollows.getFromMember().getId());
            long avgRating = Math.round(avg==null? 0: avg);
            return new FollowerListResponse(userFollows, productCnt, followerCnt, isFollowing, followCnt, avgRating);
        });
    }

    public Page<FollowingListResponse> getFollowingList(Long fromId, int page, int size) throws Exception {
        List<UserFollows> list = followRepository.findAllByFromMemberId(fromId);
        Page<UserFollows> userFollowsPage = followRepository.findAllByFromMemberId(fromId, PageRequest.of(page, size));

        return userFollowsPage.map(userFollows -> {
            Long productCnt = productRepository.countByMemberId(userFollows.getToMember().getId());
            Long followerCnt = followRepository.countByToMemberId(userFollows.getToMember().getId());
            boolean isFollowing = followRepository.existsByFromMemberIdAndToMemberId(fromId, userFollows.getToMember().getId());
            int followCnt = list.size();

            // 현재 팔로워에 대한 평점
            Double avg = reviewRepository.findByAvgRatingBySellerId(userFollows.getToMember().getId());
            long avgRating = Math.round(avg==null? 0: avg);
            return new FollowingListResponse(userFollows, productCnt, followerCnt, isFollowing, followCnt, avgRating);
        });
    }

    public Page<SellerFollowerListResponse> getSellerFollowerList(Long toId, Long userId, int page, int size) throws Exception {
        List<UserFollows> list = followRepository.findAllByToMemberId(toId);
        Page<UserFollows> userFollowsPage = followRepository.findAllByToMemberId(toId, PageRequest.of(page, size));

        return userFollowsPage.map(userFollows -> {
            Long productCnt = productRepository.countByMemberId(userFollows.getFromMember().getId());
            Long followerCnt = followRepository.countByToMemberId(userFollows.getFromMember().getId());
            boolean isFollowing = userId!=0 && followRepository.existsByFromMemberIdAndToMemberId(userId, userFollows.getFromMember().getId());
            int followCnt = list.size();
            // 현재 팔로워에 대한 평점
            Double avg = reviewRepository.findByAvgRatingBySellerId(userFollows.getFromMember().getId());
            long avgRating = Math.round(avg==null? 0: avg);
            return new SellerFollowerListResponse(userFollows, productCnt, followerCnt, isFollowing, followCnt, avgRating);
        });
    }

    public Page<SellerFollowingListResponse> getSellerFollowingList(Long fromId, Long userId, int page, int size) throws Exception {
        List<UserFollows> list = followRepository.findAllByFromMemberId(fromId);
        Page<UserFollows> userFollowsPage = followRepository.findAllByFromMemberId(fromId, PageRequest.of(page, size));

        return userFollowsPage.map(userFollows -> {
            Long productCnt = productRepository.countByMemberId(userFollows.getToMember().getId());
            Long followerCnt = followRepository.countByToMemberId(userFollows.getToMember().getId());
            boolean isFollowing = userId!=0 && followRepository.existsByFromMemberIdAndToMemberId(userId, userFollows.getToMember().getId());
            int followCnt = list.size();
            // 현재 팔로워에 대한 평점
            Double avg = reviewRepository.findByAvgRatingBySellerId(userFollows.getToMember().getId());
            long avgRating = Math.round(avg==null? 0: avg);
            return new SellerFollowingListResponse(userFollows, productCnt, followerCnt, isFollowing, followCnt, avgRating);
        });
    }

    public Long updateFollow(FollowUpdateRequest followUpdateRequest) throws Exception {
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
