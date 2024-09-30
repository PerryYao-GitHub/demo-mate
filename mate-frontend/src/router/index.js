import {createRouter, createWebHistory} from 'vue-router'
import {useUserStore} from "../plugin/pinia/user.ts";


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // admin
        // {
        //     path: "/admin",
        //     name: "admin",
        //     component: () => import('@/views/AdminView.vue'),
        // },

        // user
        {
            path: '/',
            component: () => import('../pages/Home.vue')
        },

        {
            path: '/home',
            component: () => import('../pages/Home.vue')
        },

        {
            path: '/team',
            component: () => import('../pages/Team.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/team/create',
            component: () => import('../pages/TeamCreate.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/team/check/:teamId',
            component: () => import('../pages/TeamCheck.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/team/edit/:teamId',
            component: () => import('../pages/TeamEdit.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/me',
            component: () => import('../pages/Me.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/me/edit',
            component: () => import('../pages/MeEdit.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/me/edit/tags/:tags',
            component: () => import('../pages/MeEditTags.vue'),
            meta: {requiresAuth: true,}
        },

        {
            path: '/me/auth',
            component: () => import('../pages/MeAuth.vue')
        },

        {
            path: '/search',
            component: () => import('../pages/Search.vue')
        },

        {
            path: '/search/result',
            component: () => import('../pages/SearchResult.vue'),
        },

        {
            path: '/user/:id',
            component: () => import('../pages/UserPage.vue')
        }
    ]
})

// 全局导航守卫
router.beforeEach((to, from, next) => {
    // 在导航守卫中调用 useUserStore
    const userStore = useUserStore();

    // 检查路由是否需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!userStore.isLogin) {
            // 如果用户没有登录，重定向到 /auth 页面
            next('/me/auth');
        } else {
            // 如果已经登录，继续导航
            next();
        }
    } else {
        // 对于不需要认证的路由，直接继续导航
        next();
    }
});

export default router;
