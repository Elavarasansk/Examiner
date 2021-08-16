import React from 'react';
import { render } from 'react-dom'
import Navigator from './navigator';
import { createStore, applyMiddleware  } from 'redux'
import { Provider } from 'react-redux'
import thunk from 'redux-thunk';
import rootReducer from './rootReducer';

const store = createStore(rootReducer, applyMiddleware (thunk));

render(
    <Provider store={store}>
      <Navigator/>
    </Provider>, document.getElementById('vexamine-candidate'));
