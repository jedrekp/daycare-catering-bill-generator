import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { DARK_THEME, LIGHT_THEME, USER_THEME } from '../../const';

@Injectable({
  providedIn: 'root'
})
export class ThemeSwitchService {
  private renderer: Renderer2

  constructor(rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null)
  }

  setNewTheme(darkModeOn: boolean) {
    if (darkModeOn) {
      this.renderer.addClass(document.body, DARK_THEME)
      localStorage.setItem(USER_THEME, DARK_THEME)
    }
    else {
      this.renderer.removeClass(document.body, DARK_THEME)
      localStorage.setItem(USER_THEME, LIGHT_THEME)
    }
  }

  isDarkTheme() {
    return this.getCurrentTheme() === DARK_THEME;
  }

  private getCurrentTheme() {
    const storedTheme = localStorage.getItem(USER_THEME)
    if (storedTheme)
      return storedTheme
    else return LIGHT_THEME
  }

}
