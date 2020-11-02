import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

export default new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/conference",
      name: "home",
      meta: {
        title: "Home Page"
      },
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "home" */ "./views/Home.vue")
    },
    {
      path: "/chairhub/home",
      name: "chairhubHome",
      meta: {
        title: "Chairhub Home Page"
      },
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () =>
        import(/* webpackChunkName: "chairhubHome" */ "./views/ChairhubHome.vue")
    },
    {
      path: "/userGuide",
      name: "userGuide",
      meta: {
        title: "User Guide"
      },
      component: () =>
        import(/* webpackChunkName: "userGuide" */ "./views/UserGuide.vue")
    },
    {
      path: "/presentation/create",
      meta: {
        title: "Create Presentation Page"
      },
      component: () =>
        import(/* webpackChunkName: "presentation" */ "./views/NewPresentation.vue"),
      props: true
    },
    {
      path: "/presentation",
      name: "presentation",
      meta: {
        title: "My Presentations"
      },
      component: () =>
        import(/* webpackChunkName: "presentation" */ "./views/Presentation.vue"),
      props: true
    },
    {
      path: "/presentation/:id",
      name: "section",
      meta: {
        title: "Section Page"
      },
      component: () =>
        import(/* webpackChunkName: "presentation" */ "./views/PresentationSection.vue"),
      props: true
    },
    {
      path: "/conference/add",
      meta: {
        title: "Add Conference Page"
      },
      component: () =>
        import(/* webpackChunkName: "conference" */ "./views/NewConference.vue"),
      props: true
    },
    {
      path: "/calendar",
      name: "calendar",
      meta: {
        title: "My Calendar"
      },
      component: () =>
        import(/* webpackChunkName: "conference" */ "./views/ViewCalendar.vue"),
      props: true
    },
    {
      path: "/conference/:id",
      name: "details",
      meta: {
        title: "Conference Details Page"
      },
      component: () =>
        import(/* webpackChunkName: "conference" */ "./views/ConferenceSection.vue"),
      props: true
    },
    {
      path: "/importData",
      name: "importData",
      meta: {
        title: "Import Data Page"
      },
      component: () =>
        import(/* webpackChunkName: "importData" */ "./views/ImportData.vue")
    },
    {
      path: "/404",
      name: "404",
      meta: {
        title: "404 Not Found"
      },
      component: () => import(/* webpackChunkName: "404" */ "./views/404.vue")
    },
    {
      path: "/",
      redirect: "/conference"
    },
    {
      path: "*",
      redirect: "/404"
    }
  ],
  scrollBehavior() {
    return { x: 0, y: 0 };
  }
});
