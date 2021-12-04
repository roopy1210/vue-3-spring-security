<template>
    <div class="wrapper">
        <h2>{{ msg }}</h2>
    </div>
</template>

<script>
import UserService from '../services/user.service'
import { ref } from 'vue'

export default {
    name: 'User',
    setup() {
        let msg = ref('')

        UserService.getAuthorityTest('user').then(
            (response) => {
                msg.value = response.data.msg
            },
            (error) => {
                console.log(error.message)
            }
        );

        return { msg }
    }
}
</script>

<style>
  .wrapper {
    display: flex;
    justify-content: center;
  }

  h2 {
    color:blueviolet;
  }
</style>