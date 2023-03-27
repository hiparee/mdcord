const checkNotificationPromise = () => {
  try {
    Notification.requestPermission().then();
  } catch (e) {
    return false;
  }

  return true;
};

const askNotificationPermission = () => {
  // 권한을 실제로 요구하는 함수
  function handlePermission(permission) {
    // 사용자의 응답에 관계 없이 크롬이 정보를 저장할 수 있도록 함
    if (!('permission' in Notification)) {
      Notification.permission = permission;
    }
  }

  if (!('Notification' in window)) {
    console.log('이 브라우저는 알림을 지원하지 않습니다.');
  } else {
    if (checkNotificationPromise()) {
      Notification.requestPermission().then(permission => {
        handlePermission(permission);
      });
    } else {
      Notification.requestPermission(function (permission) {
        handlePermission(permission);
      });
    }
  }
};

const notify = (title, msg) => {
  var options = {
    body: msg,
  };

  var notification = new Notification(title, options);
  console.log(notification);

  setTimeout(function () {
    notification.close();
  }, 3000);
};

export { checkNotificationPromise, notify, askNotificationPermission };
