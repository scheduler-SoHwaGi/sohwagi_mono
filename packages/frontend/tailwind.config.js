/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
    './pages/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        Grey_01: '#F1F4F5',
        Grey_02: '#E4E5E8',
        Grey_03: '#DADDE0',
        Grey_04: '#9EA4B6',
        Grey_05: '#74767C',
        Grey_06: '#343845',
        White: '#FFFFFF',
      },
      fontFamily: {
        pretendard: ['Pretendard'],
      },
      fontWeight: {
        medium: '500',
      },
    },
  },
  presets: [require('tailwindcss-preset-px-to-rem')],
  plugins: [],
};
