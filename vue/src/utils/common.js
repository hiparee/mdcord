export const getImageUrl = path => {
  return new URL(`/src/assets/images/${path}`, import.meta.url).href;
};

// user icon 아이디 포맷
export const userProfileIcon = item => {
  return item < 10 ? `0${item}` : item;
};

// user type 포맷 ( USER || ADMIN )
export const userType = user => (user === 'USER' ? '사용자' : '관리자');

// user 계정 사용 여부( Y || N )
export const userUseYn = user => (user === 'Y' ? '활성' : '비활성');
