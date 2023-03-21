export const getImageUrl = path => {
  return new URL(`../assets/images/${path}`, import.meta.url).href;
};

// user icon 아이디 포맷
export const userProfileIcon = item => {
  return item < 10 ? `0${item}` : item;
};
