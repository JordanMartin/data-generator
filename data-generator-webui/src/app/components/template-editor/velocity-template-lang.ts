import {languages} from "monaco-editor";
import IMonarchLanguage = languages.IMonarchLanguage;

export const velocity_lang: IMonarchLanguage = {
  defaultToken: '',
  ignoreCase: true,
  tokenPostfix: '.vm',
  brackets: [
    {token: 'delimiter.bracket', open: '{', close: '}'},
    {token: 'delimiter.parenthesis', open: '(', close: ')'},
    {token: 'delimiter.square', open: '[', close: ']'}
  ],
  keywords: [
    'in'
  ],

  symbols: /[=><!~?&|+\-*\/\^;\.,]+/,

  tokenizer: {
    root: [
      {include: '@whitespace'},
      [
        /[a-zA-Z]\w*/,
        {
          cases: {
            '@keywords': 'keyword',
            '@default': 'identifier'
          }
        }
      ],
      {include: '@strings'},
      {include: '@variables'},
      {include: '@numbers'},
      {include: '@directive'}
    ],
    directive: [
      [/#set/, 'keyword'],
      [/#foreach/, 'keyword'],
      [/#if/, 'keyword'],
      [/#elseif/, 'keyword'],
      [/#else/, 'keyword'],
      [/#end/, 'keyword']
    ],
    whitespace: [
      [/\s+/, 'white'],
      [/##.*/, 'comment'],
      [/#\*/, 'comment', '@blockComment']
    ],
    blockComment: [
      [/[^*#]+/, 'comment'],
      [/\*#/, 'comment', '@popall']
    ],
    numbers: [
      [/\d*\.\d+([eE][\-+]?\d+)?/, 'number.float'],
      [/0[xX][0-9a-fA-F_]*[0-9a-fA-F]/, 'number.hex'],
      [/\d+/, 'number']
    ],
    strings: [
      [/"/, 'string', '@endString']
    ],
    endString: [
      [/"/, 'string', '@popall'],
      [/./, 'string']
    ],
    variables: [
      [/\$[\w+\.-]+/, 'variable'],
      [/\$\{/, 'variable', '@curlyBraceVariable']
    ],
    curlyBraceVariable: [
      [/[\w+\.-]+/, 'variable'],
      [/[}]/, 'variable', '@pop']
    ]
  }
};
