/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        main: "#4200ff",
        error: "#ff0000",
        placeholder: "#6b6b6b",
      },
    },
  },
  plugins: [],
};
