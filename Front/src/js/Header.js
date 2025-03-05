import { mapGetters, mapState } from "vuex";
import cities from "../json/korean_cities.json";
export default {
  name: "AppHeader",
  data() {
    return {
      searchCity: "",
      cities: cities,
      filteredCities: [],
      showSearch: false,
      showSearchImg: true,
    };
  },
  computed: {
    ...mapGetters(["isLoginSuccess"]),
    ...mapState(["imageUrl"]),
  },
  created() {
    this.$store.dispatch("initLoginStatus");
  },
  mounted() {},
  methods: {
    enterLoginPage() {
      this.$router.push({ name: "Login" });
    },
    enterMyPage() {
      this.$router.push({ name: "MyPage" });
    },

    filterCities() {
      if (!this.searchCity) {
        this.filteredCities = [];
        return;
      }

      const search = this.searchCity.toLowerCase();
      this.filteredCities = this.cities.filter((city) =>
        city.name.toLowerCase().includes(search)
      );
    },
    selectCity(city) {
      this.searchCity = city.name;
      this.filteredCities = [];
      this.$emit("selectedCity", city);
    },
    showSearchBar() {
      this.showSearch = true;
      this.showSearchImg = false;
    },
    //애니메이션 시작하기 전
    beforeEnter(el) {
      el.style.transform = "translateX(100%)"; //오른쪽 외부에 있도록 설정
      el.style.opacity = 0; //투명하게 만듬
    },
    //화면에 나타나기 시작하는 시점
    enter(el, done) {
      el.offsetHeight; // 애니메이션 실행
      el.style.transition =
        "transform 0.3s ease-in-out, opacity 0.3s ease-in-out";
      el.style.transform = "translateX(0)";
      el.style.opacity = 1; //불투명하게 만듬
      done(); // 완료
    },
    leave(el, done) {
      el.style.transition =
        "transform 0.3s ease-in-out, opacity 0.3s ease-in-out";
      el.style.transform = "translateX(100%)";
      el.style.opacity = 0;
      done();
    },
  },
};
