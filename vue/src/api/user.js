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
 *   "memberId": "lemon4",
 *   "name": "사용자4"
 * }
 * */
const signupUser = payload => {
  return user.post(`${URL_PREFIX}/signup`, payload);
};

/**
 * 사용자 목록 조회
 * @param {object} pageable 페이징
 *
 * @returns {
 *   "totalPages": 0,
 *   "totalElements": 0,
 *   "size": 0,
 *   "content": [
 *     {
 *       "memberId": "string",
 *       "name": "string",
 *       "iconFileId": 0,
 *       "useYn": "string",
 *       "createDate": "2023-03-06T06:53:37.603Z",
 *       "createBy": "string",
 *       "updateDate": "2023-03-06T06:53:37.603Z",
 *       "updateBy": "string",
 *       "type": "ADMIN"
 *     }
 *   ],
 *   "number": 0,
 *   "sort": {
 *     "empty": true,
 *     "sorted": true,
 *     "unsorted": true
 *   },
 *   "first": true,
 *   "last": true,
 *   "numberOfElements": 0,
 *   "pageable": {
 *     "offset": 0,
 *     "sort": {
 *       "empty": true,
 *       "sorted": true,
 *       "unsorted": true
 *     },
 *     "pageNumber": 0,
 *     "pageSize": 0,
 *     "paged": true,
 *     "unpaged": true
 *   },
 *   "empty": true
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
 * @param {int} iconFileId 활성여부,
 *
 * @returns {
 *   "name": "사용자",
 *   "iconFileId": 1,
 *   "useYn": "Y"
 * }
 * */
const fetchUpdateMember = payload => {
  console.log('payload --> ', JSON.stringify(payload));
  console.log('{ ...payload } --> ', JSON.stringify({ ...payload }));
  return user.put(`${URL_PREFIX}`, payload);
};
export { fetchLogin, signupUser, fetchMembers, fetchUpdateMember };
