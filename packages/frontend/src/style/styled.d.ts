// src/style/styled.d.ts
import 'styled-components';
import { CSSProp } from 'styled-components';

declare module 'styled-components' {
  export interface DefaultTheme {
    colors: {
      Grey_01: string;
      Grey_02: string;
      Grey_03: string;
      Grey_04: string;
      Grey_05: string;
      Grey_06: string;
      White: string;
    };
    fonts: {
      Body_01: CSSProp;
      Body_02: CSSProp;
      Body_03: CSSProp;
      Body_04: CSSProp;
      Body_05: CSSProp;

      Contents_01: CSSProp;
      Contents_02: CSSProp;
    };
  }
}
