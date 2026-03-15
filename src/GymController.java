package com.fitness.gymManagementSystem.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fitness.gymManagementSystem.bean.Feedback;
import com.fitness.gymManagementSystem.bean.GymBook;
import com.fitness.gymManagementSystem.bean.GymItem;
import com.fitness.gymManagementSystem.bean.Item;
import com.fitness.gymManagementSystem.bean.Slot;
import com.fitness.gymManagementSystem.bean.SlotItem;
import com.fitness.gymManagementSystem.bean.SlotItemEmbed;
import com.fitness.gymManagementSystem.dao.FeedbackDao;
import com.fitness.gymManagementSystem.dao.GymBookDao;
import com.fitness.gymManagementSystem.dao.GymItemDao;
import com.fitness.gymManagementSystem.dao.SlotDao;
import com.fitness.gymManagementSystem.dao.SlotItemDao;
import com.fitness.gymManagementSystem.exception.OperatorException;
import com.fitness.gymManagementSystem.service.GymItemService;
import com.fitness.gymManagementSystem.service.GymUserService;

@RestController
public class GymController {
	
	@Autowired
	private GymBookDao gymBookDao;
	
    @Autowired
    private GymItemDao gymItemDao;
    
    @Autowired
    private SlotDao slotDao;
    
    @Autowired
    private SlotItemDao slotItemDao;
    
    @Autowired
    private GymItemService itemService;
    
    @Autowired
    private GymUserService userService;
    
    @Autowired
    private FeedbackDao feedbackDao;
    
    
    @GetMapping("/index")
    public ModelAndView showIndex() {
    	String indexPage="";
    	String userType=userService.getType(); 
    	if(userType.equalsIgnoreCase("Admin"))
    		indexPage="index1";
    	else if(userType.equalsIgnoreCase("Customer"))
    		indexPage="index2";
    	ModelAndView mv=new ModelAndView(indexPage);
    	return mv;
    }
    @GetMapping("/gymitem")
    public ModelAndView showItemEntryPage() {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
            GymItem gymItem = new GymItem();
            Long newId=gymItemDao.generateItemId();
            List<GymItem> itemList=gymItemDao.displayAllItems();
            gymItem.setItemId(newId);
            ModelAndView mv = new ModelAndView("gymItemEntryPage");
            mv.addObject("itemRecord", gymItem);
            mv.addObject("itemList",itemList);
            return mv;
    		
    	}
    	else {
    		throw new OperatorException("Page Not Found");
    	}
    }
    @PostMapping("/gymitem")
    public ModelAndView saveItemEntryPage(@ModelAttribute("itemRecord") GymItem gymItem) {
    	gymItemDao.saveNewItem(gymItem);
    	return new ModelAndView("redirect:/index");
    }    
    @GetMapping("/gymitem/edit/{id}")
    public ModelAndView showEditItemPage(@PathVariable("id") Long id) {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		GymItem gymItem = gymItemDao.findItemById(id);
            String itemName=gymItemDao.findItemName(id);
            ModelAndView mv = new ModelAndView("editGymItemPage");
            mv.addObject("itemRecord", gymItem);
            mv.addObject("itemName",itemName);
            return mv;
    	}
    	else {
    		throw new OperatorException("Page Not Found");
    	}
    }
    @PostMapping("/gymitem/update")
    public ModelAndView updateItemEntryPage(@ModelAttribute("itemRecord") GymItem gymItem) {
        gymItemDao.saveNewItem(gymItem);
        return new ModelAndView("redirect:/gymitems");
    }
    @GetMapping("/gymitems")
    public ModelAndView showItemReportPage() {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		List<GymItem> itemList=gymItemDao.displayAllItems();
        	if(!itemList.isEmpty()) {
        		ModelAndView mv=new ModelAndView("gymItemReportPage");
            	mv.addObject("itemList",itemList);
            	return mv;
        	}
        	else {
        		throw new OperatorException("No Gym Item Available");
        	}
    	}
    	else {
    		throw new OperatorException("Page not found");
    	}
    }
    @GetMapping("/slotentry")
    public ModelAndView showSlotEntryPage() {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		Slot slot = new Slot();
            Long newId=slotDao.generateSlotId();
            List<Slot> slotList=slotDao.displayAllSlot();
            slot.setSlotId(newId);
            ModelAndView mv = new ModelAndView("slotEntryPage");
            mv.addObject("slotList", slotList);
            mv.addObject("slotRecord", slot);
            return mv;
    	}
    	else {
    		throw new OperatorException("Page not found");
    	}
    }
    @PostMapping("/slotentry")
    public ModelAndView saveSlotEntryPage(@ModelAttribute("slotRecord") Slot slot) {
    	slotDao.saveNewSlot(slot);
    	List<GymItem> itemList=gymItemDao.displayAllItems();
    	for(GymItem item:itemList) {
    		SlotItemEmbed embed=new SlotItemEmbed(slot.getSlotId(),item.getItemId());
    		SlotItem slotItem=new SlotItem(embed);
    		slotItemDao.save(slotItem);
    	}
    	return new ModelAndView("redirect:/index");
    }
    @GetMapping("/slotreport")
    public ModelAndView showSlotReportPage() {
    	List<Slot> slotList=slotDao.displayAllSlot();
    	if(!slotList.isEmpty()) {
        	ModelAndView mv=new ModelAndView("slotReportPage");
        	mv.addObject("slotList",slotList);
        	return mv;
    	}
    	else {
    		throw new OperatorException("No Slots Available");
    	}
    }    
    @GetMapping("/slot-book/{id}")
    public ModelAndView showSlotBookPage(@PathVariable Long id){
    	String fname="";
    	String userType=userService.getType();
    	if(userType.equalsIgnoreCase("Admin"))
    		fname="slotBookPage1";
    	else if(userType.equalsIgnoreCase("Customer"))
    		fname="slotBookPage2";
    	Slot slot=slotDao.findSlotById(id);
    	List<Item> itemList=itemService.getItemList(id);
    	ModelAndView mv=new ModelAndView(fname);
    	mv.addObject("slot", slot);
    	mv.addObject("itemList",itemList);
    	if(userType.equalsIgnoreCase("Admin")) {
    		List<String> userList=userService.getAllCustomers();
    		mv.addObject("userList",userList);
    	}
    	return mv;
    }
    @PostMapping("/slot-book")
    public ModelAndView saveSlotBookPage(@RequestParam("slot_id") Long slotId,@RequestParam("selectItem") Long itemId,@RequestParam("userId") String userId) {
    	String userType=userService.getType();
    	String username="";
        if (userId != null && !userId.isEmpty()) {
        	if(userType.equalsIgnoreCase("Admin")) {
        		username=userId;
        	}
        } else {
        	username=userService.getUser().getUsername();
        }
        List<GymBook> book=gymBookDao.findBySlotIdAndUsername(slotId,username);
        boolean isItemAvailable = slotItemDao.isItemIdAvailable(itemId);
        if(book.isEmpty()) {
        	if (isItemAvailable) {
                GymItem gymItem = gymItemDao.findItemById(itemId);
                SlotItemEmbed embed = new SlotItemEmbed(slotId, itemId);
                int totalSeat = gymItem.getTotalSeat();
                SlotItem slotItem = slotItemDao.findItemById(embed);
                int seatBooked = slotItemDao.findSeatBookedById(embed);
                int available = totalSeat - seatBooked;

                if (available > 0) {
                    long bookingId = gymBookDao.generateBookingid();
                    seatBooked++;
                    slotItem.setSeatBooked(seatBooked);
                    GymBook gymBook = new GymBook(bookingId, slotId, itemId, username);
                    gymBookDao.save(gymBook);
                    slotItemDao.save(slotItem);

                    return new ModelAndView("redirect:/book-success/" + gymBook.getBookingId());
                } else {
                    throw new OperatorException("Seat Not Available");
                }
            } else {
                throw new OperatorException("Gym Item is not added to slot");
            }
        }
        else {
        	throw new OperatorException("You have booked slot in this timing  change the slot or cancel your booking");
        }
    }
    @GetMapping("/book-success/{bookingId}")
    public ModelAndView showSuccessPage(@PathVariable Long bookingId) {
        GymBook booking = gymBookDao.findBookInfoById(bookingId);
        String itemName=gymItemDao.findItemName(gymBookDao.findItemId(bookingId));
        ModelAndView mv = new ModelAndView("bookingSuccessPage");
        mv.addObject("booking", booking);
        mv.addObject("itemName", itemName);
        return mv;
    }
    @GetMapping("/slot-item-add/{id}")
    	public ModelAndView saveItemSlots(@PathVariable Long id) {
    	List<Slot> slot=slotDao.displayAllSlot();
    	if(!slot.isEmpty()) {
        	itemService.addNewItemToSlots(id);
        	return new ModelAndView("redirect:/index");
    	}
    	else {
    		throw new OperatorException("No Slots available");
    	}
    }
    @GetMapping("/booked")
    public ModelAndView showBookingPage() {
    	String username="";
    	String userType=userService.getType();
    	List<GymBook> bookList=gymBookDao.getBookList();
    	if(userType.equalsIgnoreCase("Customer")) {
    		username=userService.getUser().getUsername();
            bookList=gymBookDao.getEntitiesByUsername(username);
    	}
    	if(!bookList.isEmpty()) {
    		ModelAndView mv=new ModelAndView("bookedSlotPage");
        	mv.addObject("gbList", bookList);
        	return mv;
    	}
    	else {
    		throw new OperatorException("You have not Booked any slot");
    	}
    }
    @PostMapping("/booked")
    public ModelAndView cancelBooking(@RequestParam("selectBookingId") Long bookingId) {
        GymBook gymBook = gymBookDao.findBookInfoById(bookingId);    
        if (gymBook != null) {
            SlotItemEmbed embed = new SlotItemEmbed(gymBook.getSlotId(), gymBook.getItemId());
            SlotItem slotItem = slotItemDao.findItemById(embed);
            if (slotItem != null) {
                int seatBooked = slotItem.getSeatBooked();
                if (seatBooked > 0) {
                    seatBooked--;
                    slotItem.setSeatBooked(seatBooked);
                    slotItemDao.save(slotItem);
                    gymBookDao.deleteById(bookingId);
                }
            }
        }

        return new ModelAndView("redirect:/index");
    }
    @GetMapping("/customer-modification")
    public ModelAndView listCustomerPage() {
    	String usertype=userService.getType();
    	List<String> userName=userService.getAllCustomers();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		ModelAndView mv=new ModelAndView("listCustomerPage");
    		mv.addObject("list",userName);
    		return mv;
    	}
    	else {
    		throw new OperatorException("Page not found");
    	}
    	
    }
    @GetMapping("/delete-customer/{username}")
    public ModelAndView deleteCustomer(@PathVariable String username) {
    	List<GymBook> book=gymBookDao.getEntitiesByUsername(username);
    	if(book.isEmpty()) {
        	userService.removeItem(username);
        	return new ModelAndView("redirect:/customer-modification");
    	}
    	else {
    		throw new OperatorException(username+" booked slots. If you want to delete "+username+" cancel the booking first");
    	}
    }
    @ExceptionHandler(OperatorException.class)
      public ModelAndView handleSeatNotFoundException(OperatorException exception){
    	ModelAndView mv = new ModelAndView("exceptionPage");
        mv.addObject("errorMessage", exception.getMessage());
        return mv;
      }
    @GetMapping("/feedback")
    public ModelAndView feedbackpage() {
    	Feedback fb = new Feedback();
        ModelAndView mv = new ModelAndView("feedbackFormPage");
        mv.addObject("feedback",fb);
        return mv;
    }
    @PostMapping("submit-feedback")
    public ModelAndView feedBackSubmissionPage(@ModelAttribute("feedback") Feedback feedback) {
    	feedback.setUsername(userService.getUser().getUsername());
    	feedback.setEmail(userService.getUser().getEmail());
    	feedbackDao.save(feedback);
    	return new ModelAndView("redirect:index");
    }
    @GetMapping("/feedback-report")
    public ModelAndView showFeedbackReportpage() {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		List<Feedback> feedback = feedbackDao.getFeedbackList();
        	ModelAndView mv=new ModelAndView("feedbackReportPage");
        	mv.addObject("feedback",feedback);
        	return mv;
    	}
    	else {
    		throw new OperatorException("Page not found");
    	}
    }
    @GetMapping("/edit-slot")
    public ModelAndView showSlotEditPage() {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		List<Slot> slotList=slotDao.displayAllSlot();
        	if(!slotList.isEmpty()) {
            	ModelAndView mv=new ModelAndView("adminSlotReportPage");
            	mv.addObject("slotList",slotList);
            	return mv;
        	}
        	else {
        		throw new OperatorException("No Slots Available");
        	}
    	}
    	else {
    		throw new OperatorException("Page not found");
    	}
    }    
    @GetMapping("/slot/edit/{id}")
    public ModelAndView showEditSlotPage(@PathVariable("id") Long id) {
    	String usertype=userService.getType();
    	if(usertype.equalsIgnoreCase("Admin")) {
    		Slot slot= slotDao.findSlotById(id);
            ModelAndView mv = new ModelAndView("editSlotPrice");
            mv.addObject("slotRecord", slot);
            return mv;
    	}
    	else {
    		throw new OperatorException("Page Not Found");
    	}
    }
    @PostMapping("/slot/update")
    public ModelAndView updateSlotPage(@ModelAttribute("slotRecord") Slot slot) {
        slotDao.saveNewSlot(slot);
        return new ModelAndView("redirect:/edit-slot");
    }
    
}


