export default function authHeader() {
    let user = JSON.parse(localStorage.getItem('user'));

    if (user && user.accessToken) {
        return { 'authorization': 'Bearer ' + user.accessToken,
                 'username' : user.username };
    }
    else {
        return {};
    }
}
