import { ref } from 'vue'
import axios from 'axios'

const error = ref(null)
const isPending = ref(false)

const API_URL = 'http://localhost:7070/user/signup'

const signup = async (username, password, nickname) => {
    error.value = null
    isPending.value = true

    try {
        let data = {
            username: username,
            password: password,
            nickname: nickname
        }   
        let res =  axios.post(API_URL, JSON.stringify(data), {
            headers: {
                "Content-Type": 'application/json',
            },
        })
        .then(response => {
            // 사용자등록 결과 코드
            console.log('[' + username + '] 사용자 등록이 정상적으로 처리 되었습니다.')
        })
        
        error.value = null
        isPending.value = false
    } catch(err) {
        console.log(err.message)
        error.value = err.message
        isPending.value = false
    }

}

const useSignup = () => {

    return { error, signup, isPending }

}

export default useSignup