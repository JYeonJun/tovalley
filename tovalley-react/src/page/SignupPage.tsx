import React, { useState } from "react";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import styles from "../css/user/SignupPage.module.css";
import axios from "axios";

const localhost = "http://localhost:8081";

const SignupPage = () => {
  const [inputInfo, setInputInfo] = useState({
    name: "",
    email: "",
    nickName: "",
    code: "",
  });
  const [password, setPassword] = useState({
    password: "",
    confirmPassword: "",
  });
  const [available, setAvailable] = useState({
    check: false,
    available: 0,
    alert: "한글, 영어, 숫자 포함 20자 이내",
  });

  const [authSubmit, setAuthSubmit] = useState({
    emailDuplication: 0,
    authConfirm: false,
    emailAvailable: false,
  });

  const KAKAO_AUTH_URL = `http://localhost:8081/oauth2/authorization/kakao`;
  const GOOGLE_AUTH_URL = `http://localhost:8081/oauth2/authorization/google`;
  const NAVER_AUTH_URL = `http://localhost:8081/oauth2/authorization/naver`;

  const kakaoLogin = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  const googleLogin = () => {
    window.location.href = GOOGLE_AUTH_URL;
  };

  const naverLogin = () => {
    window.location.href = NAVER_AUTH_URL;
  };

  const checkNickname = (e: React.ChangeEvent<HTMLInputElement>) => {
    const regExp = /^[가-힣a-zA-Z0-9]{1,10}$/;
    if (regExp.test(e.target.value) === true) {
      setAvailable({ ...available, check: true });
    } else {
      setAvailable({ ...available, check: false });
    }
  };

  const checkDuplication = () => {
    const data = {
      nickname: inputInfo.nickName,
    };

    axios
      .post(`${localhost}/api/members/check-nickname`, data)
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          setAvailable({
            ...available,
            available: 1,
            alert: res.data.msg,
          });
        }
      })
      .catch((err) => {
        console.log(err);
        if (err.response.status === 400) {
          setAvailable({
            ...available,
            available: 2,
            alert: err.response.data.msg,
          });
        }
      });
  };

  const checkEmailDuplication = () => {
    const config = {
      params: {
        email: inputInfo.email,
      },
    };

    axios
      .get(`${localhost}/api/members/find-id`, config)
      .then((res) => {
        console.log(res);
        res.status === 200
          ? setAuthSubmit({ ...authSubmit, emailDuplication: 1 })
          : setAuthSubmit({ ...authSubmit, emailDuplication: 2 });
      })
      .then((err) => console.log(err));
  };

  const authEmail = () => {
    const data = {
      email: inputInfo.email,
    };

    console.log(data);
    setAuthSubmit({ ...authSubmit, authConfirm: true });

    axios
      .post(`${localhost}/api/email-code`, data)
      .then((res) => {
        console.log(res);
      })
      .catch((err) => console.log(err));
  };

  const authCode = () => {
    const config = {
      params: {
        email: inputInfo.email,
        verifyCode: inputInfo.code,
      },
    };

    axios
      .get(`${localhost}/api/email-code`, config)
      .then((res) => {
        console.log(res);
        res.status === 200 &&
          setAuthSubmit({
            ...authSubmit,
            authConfirm: false,
            emailAvailable: true,
          });
      })
      .catch((err) => console.log(err));
  };

  const handleSignUp = () => {
    const data = {
      name: inputInfo.name,
      email: inputInfo.email,
      nickname: inputInfo.nickName,
      password: password.password,
    };

    if (
      password.password === password.confirmPassword &&
      available.available === 1 &&
      authSubmit.emailAvailable
    ) {
      axios
        .post(`${localhost}/api/members`, data)
        .then((res) => {
          console.log(res);
          res.status === 200 && window.location.replace("/");
        })
        .catch((err) => console.log(err));
    } else {
      console.log("가입 불가");
    }
  };

  return (
    <>
      <Header />
      {/* 회원가입 컨테이너 */}
      <div className={styles.body}>
        <form className={styles.signupContainer}>
          <span>회원가입</span>
          <div className={styles.signupInput}>
            <div>
              <span>이름</span>
              <input
                required
                value={inputInfo.name}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, name: e.target.value });
                }}
              />
            </div>
            <div>
              <span>이메일</span>
              <input
                type="email"
                required
                value={inputInfo.email}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, email: e.target.value });
                }}
              />
              <span
                className={
                  authSubmit.authConfirm || inputInfo.email === ""
                    ? styles.disableBtn
                    : styles.confirmBtn
                }
                onClick={() => {
                  if (authSubmit.emailAvailable) return;
                  else {
                    authSubmit.emailDuplication !== 1
                      ? checkEmailDuplication()
                      : !authSubmit.authConfirm && authEmail();
                  }
                }}
              >
                {authSubmit.emailDuplication !== 1
                  ? "중복확인"
                  : authSubmit.emailAvailable
                  ? "사용가능"
                  : "인증"}
              </span>
              {authSubmit.emailDuplication !== 0 && (
                <span
                  className={styles.emailAlert}
                  style={
                    authSubmit.emailDuplication === 1
                      ? { color: "#38A612" }
                      : { color: "#EA0E00" }
                  }
                >
                  {authSubmit.emailDuplication === 1
                    ? "사용 가능한 이메일입니다."
                    : "이미 가입된 이메일입니다."}
                </span>
              )}
            </div>
            {authSubmit.authConfirm && (
              <div id={styles.authConfirmInfo}>
                <span>이메일로 인증코드를 전송하였습니다.</span>
                <span>00:00</span>
                <input
                  placeholder="인증코드"
                  value={inputInfo.code}
                  onChange={(e) => {
                    setInputInfo({ ...inputInfo, code: e.target.value });
                  }}
                />
                <span className={styles.authBtn} onClick={authCode}>
                  확인
                </span>
              </div>
            )}
            <div>
              <span>닉네임</span>
              <input
                required
                value={inputInfo.nickName}
                onChange={(e) => {
                  setInputInfo({ ...inputInfo, nickName: e.target.value });
                }}
                onBlur={checkNickname}
                maxLength={20}
              />
              <span
                className={
                  available.check ? styles.confirmBtn : styles.disableBtn
                }
                onClick={() => {
                  available.check && checkDuplication();
                }}
              >
                중복확인
              </span>
              <span className={styles.charCnt}>
                {inputInfo.nickName.length}/20
              </span>
            </div>
            <span
              className={
                available.available === 0
                  ? styles.alertDefault
                  : available.available === 1
                  ? styles.availAlert
                  : styles.alert
              }
            >
              {available.alert}
            </span>
            <div>
              <span>비밀번호</span>
              <input
                required
                type="password"
                value={password.password}
                onChange={(e) => {
                  setPassword({ ...password, password: e.target.value });
                }}
              />
            </div>
            <div>
              <span>비밀번호 확인</span>
              <input
                required
                type="password"
                value={password.confirmPassword}
                onChange={(e) => {
                  setPassword({ ...password, confirmPassword: e.target.value });
                }}
              />
              <span
                className={
                  password.password === password.confirmPassword
                    ? styles.alertNone
                    : styles.alert
                }
              >
                비밀번호가 일치하지 않습니다.
              </span>
            </div>
          </div>
          <div className={styles.signupBtn}>
            <button onClick={() => handleSignUp()}>가입하기</button>
          </div>
        </form>
        <div className={styles.socialSignup}>
          <div className={styles.socialSignupTitle}>
            <hr />
            <span>간편 회원가입</span>
            <hr />
          </div>
          <div className={styles.socialSignupLogo}>
            <img
              src={process.env.PUBLIC_URL + "/img/login/kakao-logo.png"}
              alt="kakao logo"
              width="40px"
              onClick={kakaoLogin}
            />
            <img
              src={process.env.PUBLIC_URL + "/img/login/naver-logo.png"}
              alt="naver logo"
              width="40px"
              onClick={naverLogin}
            />
            <img
              src={process.env.PUBLIC_URL + "/img/login/google-logo.png"}
              alt="google logo"
              width="40px"
              onClick={googleLogin}
            />
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default SignupPage;
