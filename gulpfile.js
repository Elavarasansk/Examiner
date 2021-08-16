const gulp = require('gulp');
const gulpIf = require('gulp-if');
const watch = require('gulp-watch');
const debug = require('gulp-debug');
const eslint = require('gulp-eslint');
const revts = require('gulp-rev-timestamp');
const esformatter = require('gulp-esformatter');

const webpack = require('webpack');
const gulpWebpack = require('webpack-stream');

const del = require('del');
const path = require('path');
const argv = require('yargs').argv;


//const locationDir = '/';
const locationDir = 'src/main/resources/static/built/';
const mainScriptLocation = 'src/main/js/';
const appEntry = `${mainScriptLocation}index.js`;


const scriptTargetDir = 'target/classes/static/built';
const htmlTargetDir = 'target/classes/static/';

// packs all files in the mentioned location and places in the static/built via webpack
gulp.task('webpack', () => {
  const webpackConfig = require('./webpack.config.js');
  gulp.src(appEntry)
    .pipe(gulpWebpack(webpackConfig, webpack))
    .pipe(gulp.dest(locationDir))
    .pipe(gulp.dest(scriptTargetDir));

  return gulp.src('src/main/resources/static/home.html')
    .pipe(gulp.dest(htmlTargetDir));
});

//watch script changes
gulp.task('watch:script', () => {
  const webpackConfig = require('./webpack.config.js');
  webpackConfig.watch = true;
  gulp.src(appEntry)
    .pipe(gulpWebpack(webpackConfig, webpack))
    .pipe(gulp.dest(locationDir))
    .pipe(debug({ title: 'synchronising:' }))
    .pipe(gulp.dest(scriptTargetDir));

  return gulp.src('src/main/resources/static/home.html')
    .pipe(revts({ strict: false, mode: 'timestamp' }))
    .pipe(gulp.dest(htmlTargetDir));
});

gulp.task('watch', ['clean', 'watch:script']);

gulp.task('format', () => gulp.src([`${mainScriptLocation}**/*.js`])
    .pipe(esformatter({ indent: { value: '  ' } }))
    .pipe(gulp.dest(mainScriptLocation)));

gulp.task('clean', () => del(['src/main/resources/static/built/**', 'target/classes/static/built/**']));

gulp.task('default', ['clean', 'webpack', 'watch'], () => {
  return;
});

function isFixed(file) {
  // Has ESLint fixed the file contents?
  return file.eslint != null && file.eslint.fixed;
}

gulp.task('eslint', () => gulp.src('src/main/js/**/*')
  .pipe(eslint({ fix: true }))
  .pipe(eslint.format())
  .pipe(gulpIf(isFixed, gulp.dest('src/main/js/'))));

/*var gulp = require('gulp');
var webpack = require('webpack-stream');
gulp.task('default', function() {
    return gulp.src('src/main/resources/static/home.html')
        .pipe(webpack(require('./webpack.config.js')))
        .pipe(gulp.dest('target/classes/static/'));
});*/
