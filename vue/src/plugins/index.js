import dayjs from 'dayjs';

import relativeTime from 'dayjs/plugin/relativeTime';
import 'dayjs/locale/ko';

const plugins = {
  install(app) {
    dayjs.extend(relativeTime);
    dayjs.locale('ko');
    app.config.globalProperties.$dayjs = dayjs;
    app.provide('dayjs', dayjs);
    app.use(dayjs);
  },
};

export default plugins;
