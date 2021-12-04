import axios from 'axios'
import authHeader from './auth-header'

const API_URL = 'http://localhost:7070/api/test/'

class UserService {
    getAuthorityTest(prefix) {
        return axios.get(API_URL + prefix, { headers: authHeader() });
    }
}

export default new UserService();