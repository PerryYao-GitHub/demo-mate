import Home from "../pages/Home.vue";
import Team from "../pages/Team.vue";
import Me from "../pages/Me.vue"
import Search from "../pages/Search.vue"
import Edit from "../pages/Edit.vue";
import SearchResult from "../pages/SearchResult.vue";

const routes = [
    {
        path: '/home',
        component: Home,
    },

    {
        path: '/team',
        component: Team,
    },

    {
        path: '/me',
        component: Me,
    },

    {
        path: '/edit',
        component: Edit,
    },

    {
        path: '/search',
        component: Search,
    },

    {
        path: '/search/result',
        component: SearchResult,
    },
]

export default routes
