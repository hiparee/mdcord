import { user } from './index';

const URL_PREFIX = `${import.meta.env.VITE_APP_BASE_API_URL}/members`;

const fetchLogin = async params => {
  try {
    const res = await user.post(`${URL_PREFIX}/signin`, params);
    // console.log(res);
    return res;
  } catch (error) {
    console.log(error);
  }
};

/**
 * 사용자 정보 등록
 * @param {string} memberId 아이디,
 * @param {string} name 이름,
 * @param {string} password 비밀번호
 *
 * @returns {
 *   {
 *   "memberId": "lemon4",
 *   "name": "사용자4"
 * }
 * }
 * */
const signupUser = payload => {
  return user.post(`${URL_PREFIX}/signup`, payload);
};

/**
 * 사용자 목록 조회 (페이징 처리 보류)
 * @returns {
 *   [
 *   {
 *     "memberId": "awdr81818",
 *     "name": "ajdkfjsdf",
 *     "iconFileId": 41,
 *     "useYn": "Y",
 *     "createDate": "2023-03-15T16:37:24",
 *     "createBy": "lemon",
 *     "updateDate": null,
 *     "updateBy": null,
 *     "type": "USER"
 *   },{}
 * ]
 * }
 * */
const fetchMembers = payload => {
  return user.get(`${URL_PREFIX}`, payload);
};

/**
 * 사용자 정보 수정
 * @param {string} memberId 아이디,
 * @param {string} name 이름,
 * @param {string} password 비밀번호,
 * @param {string} useYn 활성여부,
 * @param {int} iconFileId 프로필 아이콘 아이디,
 * @param {string} role 타입(사용자 |관리자 )
 *
 * @returns {
 *   {
 *   "name": "사용자",
 *   "iconFileId": 1,
 *   "useYn": "Y"
 * }
 * }
 * */
const fetchUpdateMember = payload => {
  return user.put(`${URL_PREFIX}`, payload);
};
export { fetchLogin, signupUser, fetchMembers, fetchUpdateMember };
