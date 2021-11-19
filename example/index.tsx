import { AppRegistry } from 'react-native';
import { initialize } from 'react-native-giap';
import App from './src/App';
import { name as appName } from './app.json';

initialize('photostudy-student-dev', 'https://analytics-api.got-it.ai');

AppRegistry.registerComponent(appName, () => App);
