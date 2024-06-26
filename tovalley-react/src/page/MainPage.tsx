import styles from "../css/main/MainPage.module.css";
import Header from "../component/header/Header";
import Weather from "../component/main/Weather";
import Accident from "../component/main/Accident/Accident";
import PopularValley from "../component/main/PopularValley";
import Footer from "../component/footer/Footer";
import React, { useEffect, useState } from "react";
import axios from "axios";
import RecentPost from "../component/main/RecentPost";
import { RecentPostType, RecentReviewType } from "../typings/db";

const localhost = process.env.REACT_APP_HOST;

type main = {
  nationalWeather: {
    weatherDate: string;
    dailyNationalWeather: {
      clouds: number;
      dayFeelsLike: number;
      humidity: number;
      windSpeed: number;
      region: string;
      weatherIcon: string;
      weatherDesc: string;
      minTemp: number;
      maxTemp: number;
      rainPrecipitation: number;
    }[];
  }[];
  weatherAlert: {
    weatherAlerts: {
      weatherAlertType: string;
      title: string;
      announcementTime: string;
      effectiveTime: string;
      content: string;
    }[];
    weatherPreAlerts: {
      announcementTime: string;
      title: string;
      weatherAlertType: string;
      contents: {
        content: string;
      }[];
    }[];
  };
  accidentCountDto: {
    accidentCountPerMonth: {
      month: number;
      deathCnt: number;
      disappearanceCnt: number;
      injuryCnt: number;
    }[];
    province: string;
    totalDeathCnt: number;
    totalDisappearanceCnt: number;
    totalInjuryCnt: number;
  };
  nationalPopularWaterPlaces: {
    waterPlaceId: number;
    waterPlaceName: string;
    waterPlaceImageUrl: string;
    location: string;
    rating: number;
    reviewCnt: number;
  }[];
  recentReviews: RecentReviewType[];
  recentLostFoundBoards: RecentPostType[];
};

const MainPage = () => {
  const [loading, setLoading] = useState(false);
  const [main, setMain] = useState<main>({
    nationalWeather: [
      {
        weatherDate: "",
        dailyNationalWeather: [
          {
            clouds: 0,
            dayFeelsLike: 0,
            humidity: 0,
            windSpeed: 0,
            region: "",
            weatherIcon: "",
            weatherDesc: "",
            minTemp: 0,
            maxTemp: 0,
            rainPrecipitation: 0,
          },
        ],
      },
    ],
    weatherAlert: {
      weatherAlerts: [
        {
          weatherAlertType: "",
          title: "",
          announcementTime: "",
          effectiveTime: "",
          content: "",
        },
      ],
      weatherPreAlerts: [
        {
          announcementTime: "",
          title: "",
          weatherAlertType: "",
          contents: [
            {
              content: "",
            },
          ],
        },
      ],
    },
    accidentCountDto: {
      accidentCountPerMonth: [
        {
          month: 0,
          deathCnt: 0,
          disappearanceCnt: 0,
          injuryCnt: 0,
        },
      ],
      province: "전국",
      totalDeathCnt: 0,
      totalDisappearanceCnt: 0,
      totalInjuryCnt: 0,
    },
    nationalPopularWaterPlaces: [
      {
        waterPlaceId: 0,
        waterPlaceName: "",
        waterPlaceImageUrl: "",
        location: "",
        rating: 0,
        reviewCnt: 0,
      },
    ],
    recentReviews: [
      {
        reviewId: 0,
        reviewRating: 0,
        reviewContent: "",
        reviewCreatedAt: "",
        waterPlaceId: 0,
      },
    ],
    recentLostFoundBoards: [
      {
        lostFoundBoardId: 0,
        lostFoundBoardCategory: "",
        lostFoundBoardTitle: "",
        lostFoundBoardContent: "",
        lostFoundBoardCreatedAt: "",
      },
    ],
  });

  const [regionAccident, setRegionAccident] = useState({
    accidentCountPerMonth: [
      {
        month: 0,
        deathCnt: 0,
        disappearanceCnt: 0,
        injuryCnt: 0,
      },
    ],
    province: "전국",
    totalDeathCnt: 0,
    totalDisappearanceCnt: 0,
    totalInjuryCnt: 0,
  });

  const [popularValley, setPopularValley] = useState([
    {
      waterPlaceId: 0,
      waterPlaceName: "",
      waterPlaceImageUrl: "",
      location: "",
      rating: 0,
      reviewCnt: 0,
    },
  ]);

  const [recentLostPost, setRecentLostPost] = useState([
    {
      lostFoundBoardId: 0,
      lostFoundBoardCategory: "",
      lostFoundBoardTitle: "",
      lostFoundBoardContent: "",
      lostFoundBoardCreatedAt: "",
    },
  ]);

  const [recentReview, setRecentReview] = useState([
    {
      reviewId: 0,
      reviewRating: 0,
      reviewContent: "",
      reviewCreatedAt: "",
      waterPlaceId: 0,
    },
  ]);

  useEffect(() => {
    setLoading(true);
    axios
      .get(`${localhost}/api/main-page`)
      .then((res) => {
        console.log(res);
        setMain(res.data.data);
        setRegionAccident(res.data.data.accidentCountDto);
        setPopularValley(res.data.data.nationalPopularWaterPlaces);
        setRecentLostPost(res.data.data.recentLostFoundBoards);
        setRecentReview(res.data.data.recentReviews);
      })
      .catch((err) => console.log(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <div>loading</div>;
  } else
    return (
      <div className={styles.mainPage}>
        <Header />
        <div className={styles.body}>
          <div className={styles.top}>
            <Weather
              nationalWeather={main.nationalWeather}
              alert={main.weatherAlert}
            />
            <Accident
              accident={regionAccident}
              setRegionAccident={setRegionAccident}
            />
          </div>
          <div>
            <PopularValley
              place={popularValley}
              setPopularValley={setPopularValley}
            />
          </div>
          <div className={styles.recentPost}>
            <RecentPost recentLostPost={recentLostPost} />
            <RecentPost recentReviewPost={recentReview} />
          </div>
        </div>
        <Footer />
      </div>
    );
};

export default MainPage;
