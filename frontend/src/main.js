import { createApp } from 'vue';
import App from '@/App.vue';
import router from '@/router';
//cookies
import VueCookies from 'vue-cookies';
//element plus
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
//icon
import '@/assets/icon/iconfont.css';
import '@/assets/base.scss';

// //code highlighting functionality
import "highlight.js/styles/atom-one-light.css";
import 'highlight.js/lib/common';

import Request from '@/utils/Request';
import Message from '@/utils/Message';
import Confirm from '@/utils/Confirm';
import Verify from '@/utils/Verify';
import Utils from '@/utils/Utils';

// //Custom component
import Dialog from '@/components/Dialog.vue';

const app = createApp(App);
app.use(ElementPlus);
app.use(router);
app.component("Dialog", Dialog);

// //Configure global variables
app.config.globalProperties.Request = Request;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Utils = Utils;

app.config.globalProperties.VueCookies = VueCookies;
app.mount('#app')
