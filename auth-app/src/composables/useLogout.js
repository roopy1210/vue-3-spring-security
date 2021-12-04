import { ref } from 'vue'
import store from "../store"

const error = ref(null)
const isPending = ref(false)

const logout = async () => {
    error.value = null
    isPending.value = true

    try {
        await store.dispatch('auth/logout')
        isPending.value = false
    } catch (err) {
        console.log(err.message)
        error.value = err.message
        isPending.value = false
    }
}

const useLogout = () => {
    return { logout, error, isPending }
}

export default useLogout
