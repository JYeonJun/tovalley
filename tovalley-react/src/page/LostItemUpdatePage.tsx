import React, { useEffect, useState } from "react";
import styles from "../css/lostItem/LostItemWrite.module.css";
import Header from "../component/header/Header";
import Footer from "../component/footer/Footer";
import { LuPencil } from "react-icons/lu";
import { FaPlus } from "react-icons/fa6";
import { AiFillPicture } from "react-icons/ai";
import { IoIosCloseCircle } from "react-icons/io";
import { useSaveImg } from "../composables/imgController";
import { useNavigate, useParams } from "react-router-dom";
import axiosInstance from "../axios_interceptor";
import ValleyModal from "../component/lostItem/ValleyModal";
import { PlaceName } from "../typings/db";
import ConfirmModal from "../component/common/ConfirmModal";

const LostItemUpdatePage = () => {
  const { category, id } = useParams();
  const { uploadImg, setUploadImg, imgFiles, saveImgFile, handleDeleteImage } =
    useSaveImg();
  const [currentCategory, setCurrentCategory] = useState(
    category === "LOST" ? "찾아요" : "찾았어요"
  );
  const clickCategory = (category: string) => {
    setCurrentCategory(category);
  };
  const [write, setWrite] = useState({
    title: "",
    content: "",
  });
  const [modalView, setModalView] = useState(false);
  const [selectedPlace, setSelectedPlace] = useState<PlaceName[]>([]);
  const [deleteImg, setDeleteImg] = useState<string[]>([]);
  const [confirm, setConfirm] = useState({ view: false, content: "" });
  const [alert, setAlert] = useState({ view: false, content: "" });
  const navigation = useNavigate();
  const toBack = () => {
    navigation(-1);
  };

  useEffect(() => {
    // setSelectedPlace([
    //   {
    //     waterPlaceId: -1,
    //     waterPlaceName: "금오계곡",
    //     address: "경북 구미시 금오계곡",
    //   },
    // ]);
    // setWrite({
    //   title: "금오계곡",
    //   content: "금오계곡",
    // });
    // setUploadImg([
    //   "/img/dummy/계곡이미지5.jpg",
    //   "/img/dummy/계곡이미지5.jpg",
    //   "/img/dummy/계곡이미지6.jpg",
    // ]);
    axiosInstance
      .get(`/api/lostItem/${id}`)
      .then((res) => {
        console.log(res);
        setSelectedPlace([
          {
            waterPlaceId: -1,
            waterPlaceName: res.data.data.waterPlaceName,
            address: res.data.data.waterPlaceAddress,
          },
        ]);
        setWrite({
          title: res.data.data.title,
          content: res.data.data.content,
        });
        setUploadImg(res.data.data.postImages);
      })
      .catch((err) => console.log(err));
  }, [id]);

  const closeModal = () => {
    setModalView(false);
  };

  const toListPage = () => {
    window.location.replace("/lost-item");
  };

  const writeLostPost = (e: any) => {
    e.preventDefault();
    const formData = new FormData();

    let currentCategory = "찾아요" ? "LOST" : "FOUND";

    if (
      selectedPlace.length === 0 ||
      write.title === "" ||
      write.content === ""
    ) {
      setAlert({ view: true, content: "항목을 모두 입력해주세요." });
    } else {
      formData.append("category", currentCategory);
      formData.append("waterPlaceId", `${selectedPlace[0].waterPlaceId}`);
      formData.append("title", write.title);
      formData.append("content", write.content);
      if (imgFiles.length !== 0) {
        for (let i = 0; i < imgFiles.length; i++) {
          formData.append("postImage", imgFiles[i]);
        }
      }
      if (deleteImg.length !== 0) {
        for (let i = 0; i < deleteImg.length; i++) {
          formData.append("deleteImage", deleteImg[i]);
        }
      }

      axiosInstance
        .patch("/api/auth/lostItem", formData)
        .then((res) => {
          console.log(res);
          res.status === 201 &&
            setConfirm({
              view: true,
              content: "글이 등록되었습니다.",
            });
        })
        .catch((err) => console.log(err));
    }
  };
  return (
    <div>
      <Header />
      <form className={styles.body} encType="multipart/form-data">
        <div className={styles.writeContainer}>
          <div className={styles.contentWrap}>
            <div className={styles.category}>
              <h4>카테고리</h4>
              {["찾아요", "찾았어요"].map((item) => (
                <span
                  key={item}
                  className={
                    currentCategory === item
                      ? styles.clickCategory
                      : styles.categoryBtn
                  }
                  onClick={() => clickCategory(item)}
                >
                  {item}
                </span>
              ))}
            </div>
            <div className={styles.category}>
              <h4>계곡</h4>
              <span
                className={styles.categoryBtn}
                onClick={() => setModalView(true)}
              >
                계곡 선택
              </span>
              {selectedPlace.length !== 0 && (
                <span className={styles.valleySeleted}>
                  {selectedPlace[0].waterPlaceName}
                </span>
              )}
            </div>
          </div>
          <div className={styles.contentWrap}>
            <div className={styles.category}>
              <h4>주소</h4>
              <span className={styles.categoryBtn}>주소 찾기</span>
              {selectedPlace.length !== 0 && (
                <span>{selectedPlace[0].address}</span>
              )}
            </div>
          </div>
          <div className={styles.contentWrap}>
            <input
              value={write.title}
              placeholder="제목을 입력하세요"
              onChange={(e) => setWrite({ ...write, title: e.target.value })}
            />
          </div>
          <div className={styles.contentWrap}>
            <textarea
              value={write.content}
              placeholder="내용을 입력하세요"
              onChange={(e) => setWrite({ ...write, content: e.target.value })}
            />
          </div>
          {uploadImg.length === 0 && (
            <label htmlFor="input-file">
              <input
                className={styles.imgInput}
                type="file"
                accept="image/*"
                id="input-file"
                multiple
                onChange={saveImgFile}
              />
              <div className={styles.addImgIcon}>
                <AiFillPicture />
              </div>
            </label>
          )}
          {uploadImg.length !== 0 && (
            <div className={styles.previewImg}>
              {uploadImg.map((image, id) => (
                <div key={id} className={styles.imageContainer}>
                  <div>
                    <img src={`${image}`} alt={`${image}-${id}`} />
                  </div>
                  <span
                    onClick={() => {
                      handleDeleteImage(id);
                      const deleteUrlList = deleteImg.concat([`${image}`]);
                      setDeleteImg(deleteUrlList);
                    }}
                  >
                    <IoIosCloseCircle color="#F6483D" size="30px" />
                  </span>
                </div>
              ))}
              <label htmlFor="input-file">
                <input
                  className={styles.imgInput}
                  type="file"
                  accept="image/*"
                  id="input-file"
                  multiple
                  onChange={saveImgFile}
                />
                <div className={styles.addImg}>
                  <FaPlus />
                </div>
              </label>
            </div>
          )}
        </div>
        <div className={styles.uploadBtn}>
          <button onClick={writeLostPost}>
            <span>
              <LuPencil />
            </span>
            수정하기
          </button>
          <div className={styles.cancleBtn} onClick={toBack}>
            취소
          </div>
        </div>
        {modalView && (
          <ValleyModal
            selectedPlace={selectedPlace}
            setSelectedPlace={setSelectedPlace}
            closeModal={closeModal}
            writePage={true}
          />
        )}
        {confirm.view && (
          <ConfirmModal content={confirm.content} CustomFunc={toListPage} />
        )}
        {alert.view && (
          <ConfirmModal content={alert.content} handleModal={setAlert} />
        )}
      </form>
      <Footer />
    </div>
  );
};

export default LostItemUpdatePage;
