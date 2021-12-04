import axios from 'axios';

const API_URL = 'http://localhost:7070/';

class AuthService {
    /**
     * 로그인
     */
    login(username, password) {
        return axios.post(API_URL + 'signin', {
            username: username,
            password: password
        })
        .then(response => {
            if (response.data.accessToken) {
                localStorage.setItem('user', JSON.stringify(response.data));
            }

            return response.data;
        });
    }

    /**
     * 로그아웃
     */
    logout() {
        // LocalStorage 사용자 정보
        let user = JSON.parse(localStorage.getItem('user'))

        let data = {
            username: user.username
        }

        return axios.post(API_URL + 'signout', JSON.stringify(data), {
            headers: {
                "Content-Type": 'application/json',
            },
        })
        .then(response => {
            console.log(response)
            localStorage.removeItem('user');
        });
    }
}

export default new AuthService();