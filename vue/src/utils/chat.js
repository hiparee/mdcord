export const timeAgo = dateString => {
  const MINUTE = 60 * 1000; // 1분(밀리초 단위)
  const HOUR = 60 * MINUTE; // 1시간(밀리초 단위)
  const DAY = 24 * HOUR; // 1일(밀리초 단위)
  const WEEK = 7 * DAY; // 1주일(밀리초 단위)
  const MONTH = 30 * DAY; // 1개월(밀리초 단위)
  const YEAR = 12 * MONTH; // 1년(밀리초 단위)

  const date = new Date(dateString); // 입력받은 문자열을 Date 객체로 변환
  const now = new Date(); // 현재 시각

  const elapsed = now - date; // 현재 시각과 입력받은 날짜와의 차이(밀리초 단위)

  if (elapsed < MINUTE) {
    return '방금 전';
  } else if (elapsed < HOUR) {
    const minutes = Math.floor(elapsed / MINUTE);
    return `${minutes}분 전`;
  } else if (elapsed < DAY) {
    const hours = Math.floor(elapsed / HOUR);
    return `${hours}시간 전`;
  } else if (elapsed < WEEK) {
    const days = Math.floor(elapsed / DAY);
    return `${days}일 전`;
  } else if (elapsed < MONTH) {
    const weeks = Math.floor(elapsed / WEEK);
    return `${weeks}주 전`;
  } else if (elapsed < YEAR) {
    const months = Math.floor(elapsed / MONTH);
    return `${months}개월 전`;
  } else {
    const years = Math.floor(elapsed / YEAR);
    return `${years}년 전`;
  }
};
