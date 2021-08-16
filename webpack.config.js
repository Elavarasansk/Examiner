const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const TerserPlugin = require('terser-webpack-plugin');

const mode = 'production';

const getOutput = function () {
  const outputs = {
      development: {
        path: path.join(__dirname, './src/main/resources/static/built'),
        filename: '[name]_bundle.js',
        publicPath: 'built/',
      },
      production: {
        path: path.join(__dirname, './src/main/resources/static/built'),
        filename: '[name]_bundle.js',
        publicPath: 'built/',
      },
  };
  return outputs[mode];
};

module.exports = {
    entry: {
      home: ['babel-polyfill', './src/main/js/home/index.js'],
      admin: ['babel-polyfill', './src/main/js/admin/index.js'],
      manager: ['babel-polyfill', './src/main/js/manager/index.js'],
      candidate: ['babel-polyfill', './src/main/js/candidate/index.js']
    },
    output: getOutput(),
    devServer: {
      host:"0.0.0.0",
      inline: true,
      port: 9090,
      proxy: {
        '*': {
          target: 'http://localhost:2020',
        }
      }
    },
    mode,
    cache: false,
    module: {
      rules: [
        {
          test: /\.jsx?$/,
          exclude: /node_modules/,
          loader: 'babel-loader',
          query: {
            presets: ['es2015', 'react'],
            plugins: ['transform-class-properties']
          }
        },
        {
          test: /\.css$/,
          use: [ 'style-loader', 'css-loader' ]
        },
        {
          test: /\.(jpe?g|png|jpg|gif|svg|ico)$/i,
          use: [{
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
              outputPath: 'images/'
            }
          }]
        },
        {
          test: /\.(html)$/,
          use: {
            loader: 'html-loader',
            options: {
              attrs: ['img:src', 'link:href']
            }
          }
        }
        ]
    },
    optimization: {/*
      minimize: true,
      minimizer: [
        new TerserPlugin({
          terserOptions: {
            ecma: 6,
            compress: true,
            output: {
              comments: false,
              beautify: false
            }
          }
        })
        ]
    */},
    devtool: 'nosources-source-map'
}