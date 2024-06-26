package kr.ac.kumoh.illdang100.tovalley.domain.review;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.SliceImpl;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static kr.ac.kumoh.illdang100.tovalley.domain.member.QMember.member;
import static kr.ac.kumoh.illdang100.tovalley.domain.review.QReview.review;
import static kr.ac.kumoh.illdang100.tovalley.domain.trip_schedule.QTripSchedule.tripSchedule;
import static kr.ac.kumoh.illdang100.tovalley.domain.water_place.QWaterPlace.waterPlace;
import static kr.ac.kumoh.illdang100.tovalley.dto.review.ReviewRespDto.*;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<WaterPlaceReviewRespDto> findReviewsByWaterPlaceId(Long waterPlaceId,
                                                                   Long memberId,
                                                                   Pageable pageable) {

        BooleanExpression isMyReviewExpression = review.tripSchedule.member.id.eq(memberId);

        JPAQuery<WaterPlaceReviewRespDto> query = queryFactory
                .select(Projections.constructor(WaterPlaceReviewRespDto.class,
                        review.id,
                        member.imageFile.storeFileUrl,
                        member.nickname,
                        review.rating,
                        review.createdDate,
                        review.reviewContent,
                        review.waterQualityReview,
                        isMyReviewExpression
                ))
                .from(review)
                .join(review.tripSchedule, tripSchedule)
                .join(tripSchedule.member, member)
                .where(tripSchedule.waterPlace.id.eq(waterPlaceId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    review.getType(), review.getMetadata()
            );

            if ("rating".equals(o.getProperty())) {
                // 평점 먼저 정렬하고, 평점이 같은 경우 최신순으로 정렬
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
                query.orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("createdDate")));
            } else {
                // 그 외의 경우 일반적으로 정렬
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }
        }

        List<WaterPlaceReviewRespDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .join(review.tripSchedule, tripSchedule)
                .join(tripSchedule.member, member)
                .where(tripSchedule.waterPlace.id.eq(waterPlaceId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Slice<MyReviewRespDto> findSliceMyReviewByMemberId(Long memberId, Pageable pageable) {

        JPAQuery<MyReviewRespDto> query = queryFactory
                .select(Projections.constructor(MyReviewRespDto.class,
                        review.id,
                        waterPlace.id,
                        waterPlace.waterPlaceName,
                        review.rating,
                        review.createdDate,
                        review.reviewContent,
                        review.waterQualityReview
                ))
                .from(review)
                .where(member.id.eq(memberId))
                .join(review.tripSchedule, tripSchedule)
                .join(tripSchedule.member, member)
                .join(tripSchedule.waterPlace, waterPlace)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(
                    review.getType(), review.getMetadata()
            );
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<MyReviewRespDto> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }
}
