/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        main: "#4200ff",
        white: "#ffffff",
        white_sub: "#a9c3eb",
        black: "#191919",
        black_sub: "#54657b",
        red: "#ff0000",
        placeholder: "#6b6b6b",
        gray1: "#f0f0f0",
        gray2: "#d9d9d9",
      },
    },
  },
  plugins: [],
};
