import { useStore } from 'vuex'

const getUser = () => {

    const store = new useStore()
    const user = store.state.auth.user

    return { user }
}

export default getUser