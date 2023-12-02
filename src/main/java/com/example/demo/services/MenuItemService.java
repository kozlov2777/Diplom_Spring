package com.example.demo.services;

import com.example.demo.dto.ItemDetailDto;
import com.example.demo.dto.ItemsForCreateDTO;
import com.example.demo.dto.MenuItemDto;
import com.example.demo.dto.StatusesDto;
import com.example.demo.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;


    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemDto> getMenuItems() {
        return menuItemRepository.getMenuItems();
    }

    public List<ItemDetailDto> getItemsDetail(Long order_id){
        return menuItemRepository.getItemDetails(order_id);
    }
    public List<ItemsForCreateDTO> getItemsDetailForCreate(){
        return menuItemRepository.getMenuItemsForCreate();
    }
    public List<StatusesDto> statusesList(){
        return menuItemRepository.getStatusesList();
    }
}
