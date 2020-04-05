// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED = Boolean(process.env.DEBUG_INFO_ENABLED);
export const SERVER_API_URL = process.env.SERVER_API_URL;
export const BUILD_TIMESTAMP = process.env.BUILD_TIMESTAMP;
export const BASE_URL_BOOKS_TXT = 'https://www.gutenberg.org/files/';
export const BASE_URL_BOOKS = 'http://www.gutenberg.org/ebooks/';
export const NO_IMAGE = 'https://i.pinimg.com/originals/2a/3a/d9/2a3ad9e744259a63bb4fb581455409c1.jpg';
