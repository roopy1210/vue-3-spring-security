import { createApp } from 'vue'
import Axios from 'axios'
import App from './App.vue'
import router from './router'
import store from './store'

// global styles
import './assets/main.css'

// Add a response interceptor
Axios.interceptors.response.use(
  (response) => {
    console.log('★ Axios.interceptors.response executed.')    
    // LocalStorage 사용자 정보
    let user = JSON.parse(localStorage.getItem('user'))

    if (user) {
      console.log('-----------------------------------------------------------------------------')
      let currentAccessToken = user.accessToken
      console.log('currentAccessToken : ', currentAccessToken)

      // Header Acesstoken 정보
      let newAccessToken = response.headers['accesstoken']
      
      // LocalStorage Token 과 Header의 토큰이 같지 않으면 새로 발급받은 토큰을 업데이트 한다.
      if (newAccessToken) {
        console.log('newAccessToken : ', newAccessToken)

        if (newAccessToken !== currentAccessToken) {
            user.accessToken = newAccessToken
            localStorage['user'] = JSON.stringify(user);
            console.log('Token 정보가 업데이트 되었습니다.')
        }      
      }
      console.log('-----------------------------------------------------------------------------')
    }
    
    return response;
  },
  (error) => {
    console.log('★ Axios.interceptors.respoonse error executed.')    

    switch (error.response.status) {
      case 401:
        localStorage.removeItem('user');
        router.push({ name: 'Login' })
        break;
      case 403:
        console.log('[' + error.response.status + '] Error')
        router.push({ name: 'Forbidden' })
        break;
    }

    return Promise.reject(error);
  }
);  

createApp(App)
    .use(store)
    .use(router)
    .mount('#app')
