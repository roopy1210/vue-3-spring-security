import { ref } from 'vue'
import store from "../store"

const error = ref(null)
const isPending = ref(false)

const login = async (username, password) => {

    error.value = null
    isPending.value = true


    try {
        await store.dispatch('auth/login', {username, password})

        error.value = null
        isPending.value = false
    } catch(err) {
        error.value = 'Incorrect login credentials'
        isPending.value = false
    }
}

const useLogin = () => {
    return { error, login, isPending }
}

export default useLogin
