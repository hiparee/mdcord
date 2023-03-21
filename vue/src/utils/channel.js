export const makeChannelTree = data => {
  const result = [];

  // 부모 메뉴를 순회하면서 channel 속성을 추가
  data
    .filter(item => item.dept === 1) // dept가 1인 항목만 필터링
    .sort((a, b) => a.channelOrder - b.channelOrder) // channelOrder를 기준으로 정렬
    .forEach(parentMenu => {
      parentMenu.subChannel = []; // 빈 배열로 초기화

      // 하위 메뉴를 찾아서 channel 속성에 추가
      data
        .filter(item => item.parentId === parentMenu.id && item.dept === 2) // parentMenu의 하위 항목을 필터링
        .sort((a, b) => a.channelOrder - b.channelOrder) // channelOrder를 기준으로 정렬
        .forEach(channel => parentMenu.subChannel.push(channel)); // parentMenu의 channel 속성에 추가

      result.push(parentMenu); // 결과 배열에 추가
    });

  return result;
};
