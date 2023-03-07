export default instance => {
  instance.interceptors.request.use(
    config => {
      return config;
    },
    error => {
      return Promise.reject(error.response);
    },
  );

  instance.interceptors.response.use(
    config => {
      return config;
    },
    error => {
      return Promise.reject(error.response);
    },
  );
};
